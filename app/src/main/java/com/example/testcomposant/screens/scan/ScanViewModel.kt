package com.example.testcomposant.screens.scan

import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.Context.BLUETOOTH_SERVICE
import androidx.lifecycle.ViewModel
import com.example.testcomposant.ApplicationRoot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ScanViewModel : ViewModel() {
    // La liste des appareils scannés autour
    val scanItemsFlow = MutableStateFlow<List<ScanResult>>(emptyList())

    // Boolean permettant de savoir si nous sommes en train de scanner
    val isScanningFlow = MutableStateFlow(false)

    // Le processus de scan
    private var scanJob: Job? = null

    // Durée du scan
    private val scanDuration = 10000L

    /**
     * Le scanner bluetooth
     */
    // ApplicationRoot.getContext() est une référence au contexte de l'application
    // Elle est initialisée dans ApplicationRoot
    private val bluetoothLeScanner = (ApplicationRoot.getContext().getSystemService(BLUETOOTH_SERVICE) as BluetoothManager).adapter.bluetoothLeScanner
    private val scanFilters: List<ScanFilter> = emptyList()
    private val scanSettings = ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build()
    private val scanResultsSet = mutableMapOf<String, ScanResult>()

    // Objet qui sera appelé à chaque résultat de scan
    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            // On ajoute le résultat dans le set, si il n'y est pas déjà
            // L'ajout retourne null si l'élément n'était pas déjà présent
            if (scanResultsSet.put(result.device.address, result) == null) {
                // On envoie la nouvelle liste des appareils scannés
                scanItemsFlow.value = scanResultsSet.values.toList()
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun startScan() {
        if(isScanningFlow.value) return

        scanJob = CoroutineScope(Dispatchers.IO).launch {
            // On indique que nous sommes en train de scanner
            isScanningFlow.value = true

            // On lance le scan BLE a la souscription de scanFlow
            bluetoothLeScanner.startScan(scanFilters, scanSettings, scanCallback)

            delay(scanDuration)

            // Lorsque scanFlow est stoppé, on stop le scan BLE
            bluetoothLeScanner.stopScan(scanCallback)

            // On indique que nous ne sommes plus en train de scanner
            isScanningFlow.value = false
        }
    }

    @SuppressLint("MissingPermission")
    fun stopScan() {
        scanJob?.cancel()
        bluetoothLeScanner.stopScan(scanCallback)
    }

    fun clearScanItems() {
        scanResultsSet.clear()
        scanItemsFlow.value = scanResultsSet.values.toList()
    }
}