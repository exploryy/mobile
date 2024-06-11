package com.example.explory.presentation.screen.map.location

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun RequestLocationPermission(
    requestCount: Int = 0,
    onPermissionDenied: () -> Unit,
    onPermissionReady: () -> Unit,
    afterRequest: @Composable () -> Unit
) {
    val requestFinished = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissionsMap ->
        val granted = permissionsMap.values.all { it }
        if (granted) {
            onPermissionReady()
            requestFinished.value = true
        } else {
            onPermissionDenied()
            requestFinished.value = true
        }
    }
    if (requestFinished.value) {
        afterRequest()
    }
    LaunchedEffect(requestCount) {
        context.checkAndRequestLocationPermission(
            locationPermissions,
            launcher,
            onPermissionReady
        )
    }
}

private fun Context.checkAndRequestLocationPermission(
    permissions: Array<String>,
    launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>,
    onPermissionReady: () -> Unit
) {
    if (permissions.all {
            ContextCompat.checkSelfPermission(
                this,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    ) {
        onPermissionReady()
    } else {
        launcher.launch(permissions)
    }
}


private val locationPermissions = arrayOf(
    android.Manifest.permission.ACCESS_FINE_LOCATION,
    android.Manifest.permission.ACCESS_COARSE_LOCATION,
)