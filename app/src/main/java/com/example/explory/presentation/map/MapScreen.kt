package com.example.explory.presentation.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition

@Composable
fun MapScreen() {
    val context = LocalContext.current
    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {
        // State variables to manage location information and permission result text
        var locationText by remember { mutableStateOf("No location obtained :(") }
        var showPermissionResultText by remember { mutableStateOf(false) }
        var permissionResultText by remember { mutableStateOf("Permission Granted...") }
        val userPosition = remember { mutableStateOf(0.0 to 0.0) }

        // Request location permission using a Compose function
        RequestLocationPermission(onPermissionGranted = {
            // Callback when permission is granted
            showPermissionResultText = true
            // Attempt to get the last known user location
            getLastUserLocation(context = context,
                fusedLocationProviderClient = fusedLocationProviderClient,
                onGetLastLocationSuccess = {
                    userPosition.value = it
                    locationText =
                        "Location using LAST-LOCATION: LATITUDE: ${it.first}, LONGITUDE: ${it.second}"
                },
                onGetLastLocationFailed = { exception ->
                    showPermissionResultText = true
                    locationText = exception.localizedMessage ?: "Error Getting Last Location"
                },
                onGetLastLocationIsNull = {
                    // Attempt to get the current user location
                    getCurrentLocation(context = context,
                        fusedLocationProviderClient = fusedLocationProviderClient,
                        onGetCurrentLocationSuccess = {
                            locationText =
                                "Location using CURRENT-LOCATION: LATITUDE: ${it.first}, LONGITUDE: ${it.second}"
                        },
                        onGetCurrentLocationFailed = {
                            showPermissionResultText = true
                            locationText = it.localizedMessage ?: "Error Getting Current Location"
                        })
                })
        }, onPermissionDenied = {
            // Callback when permission is denied
            showPermissionResultText = true
            permissionResultText = "Permission Denied :("
        }, onPermissionsRevoked = {
            // Callback when permission is revoked
            showPermissionResultText = true
            permissionResultText = "Permission Revoked :("
        })

        // Compose UI layout using a Column
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AndroidView(modifier = Modifier.fillMaxSize(), // Occupy the max size in the Compose UI tree
                factory = { context ->
                    // Creates view
                    MapYandex(context).apply {
                        myLocationButton.setOnClickListener {
                            getCurrentLocation(context = context,
                                fusedLocationProviderClient = fusedLocationProviderClient,
                                onGetCurrentLocationSuccess = {
                                    locationText =
                                        "Location using CURRENT-LOCATION: LATITUDE: ${it.first}, LONGITUDE: ${it.second}"
                                    moveTo(latitude = it.first, longitude = it.second)
                                },
                                onGetCurrentLocationFailed = {
                                    showPermissionResultText = true
                                    locationText =
                                        it.localizedMessage ?: "Error Getting Current Location"
                                })
                        }
                    }
                }, update = { view ->
                    view.moveTo(
                        latitude = userPosition.value.first,
                        longitude = userPosition.value.second
                    )
                    view.drawCircle(
                        latitude = userPosition.value.first, longitude = userPosition.value.second
                    )
                })
        }
    }

}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocationPermission(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
    onPermissionsRevoked: () -> Unit
) {
    // Initialize the state for managing multiple location permissions.
    val permissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    // Use LaunchedEffect to handle permissions logic when the composition is launched.
    LaunchedEffect(key1 = permissionState) {
        // Check if all previously granted permissions are revoked.
        val allPermissionsRevoked =
            permissionState.permissions.size == permissionState.revokedPermissions.size

        // Filter permissions that need to be requested.
        val permissionsToRequest = permissionState.permissions.filter {
            !it.status.isGranted
        }

        // If there are permissions to request, launch the permission request.
        if (permissionsToRequest.isNotEmpty()) permissionState.launchMultiplePermissionRequest()

        // Execute callbacks based on permission status.
        if (allPermissionsRevoked) {
            onPermissionsRevoked()
        } else {
            if (permissionState.allPermissionsGranted) {
                onPermissionGranted()
            } else {
                onPermissionDenied()
            }
        }
    }
}


@SuppressLint("MissingPermission")
private fun getLastUserLocation(
    context: Context,
    onGetLastLocationSuccess: (Pair<Double, Double>) -> Unit,
    onGetLastLocationFailed: (Exception) -> Unit,
    onGetLastLocationIsNull: () -> Unit,
    fusedLocationProviderClient: FusedLocationProviderClient
) {
    // Check if location permissions are granted
    if (areLocationPermissionsGranted(context = context)) {
        // Retrieve the last known location
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                // If location is not null, invoke the success callback with latitude and longitude
                onGetLastLocationSuccess(Pair(it.latitude, it.longitude))
            }?.run {
                onGetLastLocationIsNull()
            }
        }.addOnFailureListener { exception ->
            // If an error occurs, invoke the failure callback with the exception
            onGetLastLocationFailed(exception)
        }
    }
}


/**
 * Retrieves the current user location asynchronously.
 *
 * @param onGetCurrentLocationSuccess Callback function invoked when the current location is successfully retrieved.
 *        It provides a Pair representing latitude and longitude.
 * @param onGetCurrentLocationFailed Callback function invoked when an error occurs while retrieving the current location.
 *        It provides the Exception that occurred.
 * @param priority Indicates the desired accuracy of the location retrieval. Default is high accuracy.
 *        If set to false, it uses balanced power accuracy.
 */
@SuppressLint("MissingPermission")
private fun getCurrentLocation(
    context: Context,
    onGetCurrentLocationSuccess: (Pair<Double, Double>) -> Unit,
    onGetCurrentLocationFailed: (Exception) -> Unit,
    priority: Boolean = true,
    fusedLocationProviderClient: FusedLocationProviderClient
) {
    // Determine the accuracy priority based on the 'priority' parameter
    val accuracy = if (priority) Priority.PRIORITY_HIGH_ACCURACY
    else Priority.PRIORITY_BALANCED_POWER_ACCURACY

    // Check if location permissions are granted
    if (areLocationPermissionsGranted(context = context)) {
        // Retrieve the current location asynchronously
        fusedLocationProviderClient.getCurrentLocation(
            accuracy, CancellationTokenSource().token,
        ).addOnSuccessListener { location ->
            location?.let {
                // If location is not null, invoke the success callback with latitude and longitude
                onGetCurrentLocationSuccess(Pair(it.latitude, it.longitude))
            }?.run {
                //Location null do something
            }
        }.addOnFailureListener { exception ->
            // If an error occurs, invoke the failure callback with the exception
            onGetCurrentLocationFailed(exception)
        }
    }
}


/**
 * Checks if location permissions are granted.
 *
 * @return true if both ACCESS_FINE_LOCATION and ACCESS_COARSE_LOCATION permissions are granted; false otherwise.
 */
private fun areLocationPermissionsGranted(context: Context): Boolean {
    return (ActivityCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED)
}

