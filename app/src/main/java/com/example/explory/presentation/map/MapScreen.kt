package com.example.explory.presentation.map

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.location.LocationServices
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.MutableState
import androidx.compose.ui.unit.sp
import com.google.android.gms.location.FusedLocationProviderClient
import org.koin.androidx.compose.koinViewModel
import kotlin.reflect.KFunction5

@Composable
fun MapScreen(
    viewModel: MapViewModel = koinViewModel(),
) {
    val mapState by viewModel.mapState.collectAsStateWithLifecycle()
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
        Box(
            modifier = Modifier.fillMaxSize()
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
                    if (mapState.polygons != null) {
                        view.drawCircle(
                            latitude = userPosition.value.first,
                            longitude = userPosition.value.second,
                            coordinates = mapState.polygons ?: emptyList()
                        )
                    }
//                    view.drawCircle(
//                        latitude = userPosition.value.first, longitude = userPosition.value.second
//                    )
//                    if (mapState.polygons != null) {
//                        view.drawRussia(
//                            coordinates = mapState.polygons ?: emptyList()
//                        )
//                    }
                }
            )
            BottomButtonLayout(
                fusedLocationProviderClient = fusedLocationProviderClient,
                getCurrentLocation = ::getCurrentLocation,
                context = context,
                locationTextState = remember { mutableStateOf(locationText) },
                showPermissionResultTextState = remember { mutableStateOf(showPermissionResultText) }
            )
        }
    }
}

@Composable
fun BottomButtonLayout(
    fusedLocationProviderClient: FusedLocationProviderClient,
    getCurrentLocation: KFunction5<Context, (Pair<Double, Double>) -> Unit, (Exception) -> Unit, Boolean, FusedLocationProviderClient, Unit>,
    context: Context,
    locationTextState: MutableState<String>,
    showPermissionResultTextState: MutableState<Boolean>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                getCurrentLocation(
                    context = context,
                    fusedLocationProviderClient = fusedLocationProviderClient,
                    onGetCurrentLocationSuccess = { position ->
                        locationTextState.value = "Location using CURRENT-LOCATION: LATITUDE: ${position.first}, LONGITUDE: ${position.second}"
                    },
                    onGetCurrentLocationFailed = { exception ->
                        showPermissionResultTextState.value = true
                        locationTextState.value = exception.localizedMessage ?: "Error Getting Current Location"
                    }
                )
            },
            modifier = Modifier.wrapContentSize(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xA6000000)),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text("MУ LOCATION", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { /* Handle click here */ },
                modifier = Modifier.size(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xA6000000)),
                shape = RoundedCornerShape(24.dp)
            ) {

            }

            Button(
                onClick = { /* Handle click here */ },
                modifier = Modifier.size(64.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xA6000000)),
                shape = RoundedCornerShape(28.dp)
            ) {

            }

            Button(
                onClick = { /* Handle click here */ },
                modifier = Modifier.size(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xA6000000)),
                shape = RoundedCornerShape(24.dp)
            ) {

            }
        }
    }
}


