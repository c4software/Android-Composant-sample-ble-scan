package com.example.testcomposant.screens.scan

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testcomposant.R
import com.example.testcomposant.composants.ElementList

@SuppressLint("MissingPermission")
@Composable
fun ScanScreen(
    modifier: Modifier = Modifier,
    scanViewModel: ScanViewModel = viewModel()
) {
    // La liste des appareils scannés autour
    val scanItems by scanViewModel.scanItemsFlow.collectAsStateWithLifecycle()
    // Boolean permettant de savoir si nous sommes en train de scanner
    val isScanning by scanViewModel.isScanningFlow.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Boutons en haut de l'écran (débuter le scan, arrêter le scan, vider la liste)
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 5.dp)) {
            Button(
                colors = if (isScanning) ButtonDefaults.buttonColors(containerColor = Color.Red) else ButtonDefaults.buttonColors(),
                onClick = { scanViewModel.startScan() },
                enabled = !isScanning
            ) {
                if (isScanning) Text(text = "Scan en cours") else Text(text = "Débuter le scan")
            }
            Spacer(modifier = Modifier.padding(5.dp))
            Button(onClick = { scanViewModel.clearScanItems() }) {
                Text(text = "Vider la liste")
            }
        }

        // Le scan est lancé nous affichons la liste des appareils trouvés
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(scanItems) { item ->
                ElementList(
                    title = item.device.name ?: "Sans nom",
                    content = item.device.address ?: "00:00:00:00:00:00",
                    image = R.drawable.baseline_bluetooth_24
                )
            }
        }
    }
}
