package com.example.explory.presentation.map

import android.R
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.explory.presentation.map.location.RequestLocationPermission
import com.example.explory.presentation.utils.ExampleScaffold
import com.example.explory.ui.theme.ExploryTheme
import com.mapbox.geojson.Feature
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.geojson.Polygon
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.style.layers.generated.FillColor
import com.mapbox.maps.extension.compose.style.layers.generated.FillLayer
import com.mapbox.maps.extension.compose.style.layers.generated.FillOpacity
import com.mapbox.maps.extension.compose.style.sources.generated.GeoJSONData
import com.mapbox.maps.extension.compose.style.sources.generated.GeoJsonSourceState
import com.mapbox.maps.extension.compose.style.standard.MapboxStandardStyle
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.viewport.ViewportStatus
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

const val ZOOM: Double = 0.0
const val PITCH: Double = 0.0
const val OPENED_WORLD_LAYER = "layer-parking"

@OptIn(MapboxExperimental::class)
@Composable
fun MapScreen(
    viewModel: MapViewModel = koinViewModel()
) {
    val mapState by viewModel.mapState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var permissionRequestCount by remember {
        mutableIntStateOf(1)
    }
    var showMap by remember {
        mutableStateOf(false)
    }
    var showRequestPermissionButton by remember {
        mutableStateOf(false)
    }
    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            center(Point.fromLngLat(0.0, 0.0))
            zoom(ZOOM)
            pitch(PITCH)
        }
    }


//    val outerLineString = LineString.fromLngLats(
//        listOf(
//            Point.fromLngLat(180.0, 90.0),
//            Point.fromLngLat(180.0, -90.0),
//            Point.fromLngLat(-180.0, -90.0),
//            Point.fromLngLat(-180.0, 90.0),
//            Point.fromLngLat(180.0, 90.0)
//        )
//    )


    val withHolesSourceState = remember {
        GeoJsonSourceState()
    }

//    withHolesSourceState.data = GeoJSONData(
//        Feature.fromGeometry(
//            Polygon.fromOuterInner(outerLineString, mapState.innerPoints)
//        )
//    )

    ExploryTheme {
        ExampleScaffold(floatingActionButton = {
            if (mapViewportState.mapViewportStatus == ViewportStatus.Idle) {
                FloatingActionButton(onClick = {
                    mapViewportState.transitionToFollowPuckState()
                }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_menu_mylocation),
                        contentDescription = "Locate button"
                    )
                }
            }
        }, snackBarHost = {
            SnackbarHost(snackBarHostState)
        }) {
            RequestLocationPermission(
                onPermissionDenied = {
                    scope.launch {
                        snackBarHostState.showSnackbar("You need to accept location permissions.")
                    }
                    showRequestPermissionButton = true
                },
                onPermissionGranted = {
                    showRequestPermissionButton = false
                    showMap = true
                },
                onPermissionsRevoked = {
                    scope.launch {
                        snackBarHostState.showSnackbar("You need to accept location permissions in settings.")
                    }
                    showRequestPermissionButton = true
                },
            )
            if (showMap) {
                MapboxMap(
                    Modifier.fillMaxSize(),
                    style = {
//                        MapStyle(style = Style.DARK)
                        MapboxStandardStyle(
                            topSlot = {
                                // todo fill with cloudy pattern
                                FillLayer(
                                    sourceState = withHolesSourceState,
                                    layerId = OPENED_WORLD_LAYER,
                                    fillColor = FillColor(Color.DarkGray),
                                    fillOpacity = FillOpacity(1.0)
                                )
                            }
                        )

                    },
                    mapViewportState = mapViewportState
                ) {
                    MapEffect(Unit) { mapView ->
                        mapView.location.updateSettings {
                            locationPuck = createDefault2DPuck(withBearing = true)
                            puckBearingEnabled = true
                            puckBearing = PuckBearing.HEADING
                            enabled = true
                        }
                        mapViewportState.transitionToFollowPuckState()
                    }

                }
            }
            if (showRequestPermissionButton) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.align(Alignment.Center)) {
                        Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                            onClick = {
                                permissionRequestCount += 1
                            }) {
                            Text("Request permission again ($permissionRequestCount)")
                        }
                        Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                            onClick = {
                                context.startActivity(
                                    Intent(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.fromParts("package", context.packageName, null)
                                    )
                                )
                            }) {
                            Text("Show App Settings page")
                        }
                    }
                }
            }
        }

        if (showRequestPermissionButton || showMap) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Button(
                        onClick = {  },
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(25))
                    ) {
                        Text("Left")
                    }

                    Button(
                        onClick = {  },
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(25))
                    ) {
                        Text("Center")
                    }

                    Button(
                        onClick = {  },
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(25))
                    ) {
                        Text("Right")
                    }
                }
            }
        }
    }
}