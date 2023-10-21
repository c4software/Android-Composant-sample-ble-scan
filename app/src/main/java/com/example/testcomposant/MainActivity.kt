@file:OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)

package com.example.testcomposant

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.testcomposant.models.CardContent
import com.example.testcomposant.screens.RequestPermissionsScreen
import com.example.testcomposant.screens.scan.ScanScreen
import com.example.testcomposant.ui.theme.MyApplicationTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val toCheckPermissions = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                listOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
            } else {
                listOf(android.Manifest.permission.BLUETOOTH_CONNECT, android.Manifest.permission.BLUETOOTH_SCAN)
            }
            val permissionState = rememberMultiplePermissionsState(toCheckPermissions)
            var selectedItem by remember { mutableStateOf<CardContent?>(null) }

            MyApplicationTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            colors = TopAppBarDefaults.smallTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            ),
                            navigationIcon = {
                                if (selectedItem != null) {
                                    IconButton(onClick = { selectedItem = null })
                                    { Icon(Icons.Default.ArrowBack, "Back") }
                                }
                            },
                            title = {
                                AnimatedContent(selectedItem == null, label = "") { targetState ->
                                    when (targetState) {
                                        true -> Text(getString(R.string.app_name))
                                        false -> selectedItem?.let { Text(it.title) }
                                    }
                                }
                            },
                        )
                    },
                    content = {
                        if (!permissionState.allPermissionsGranted) {
                            RequestPermissionsScreen(Modifier.padding(it), permissionState)
                        } else {
                            ScanScreen(Modifier.padding(it))
//                            ListScreen(Modifier.padding(it), selectedItem) {
//                                selectedItem = it
//                            }
                        }
                    }
                )
            }
        }
    }
}