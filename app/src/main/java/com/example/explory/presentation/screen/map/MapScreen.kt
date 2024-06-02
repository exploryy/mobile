package com.example.explory.presentation.screen.map

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.explory.R
import com.example.explory.presentation.screen.map.component.ButtonControlRow
import com.example.explory.presentation.screen.map.location.RequestLocationPermission
import com.example.explory.presentation.screen.map.notifications.RequestNotificationPermission
import com.example.explory.presentation.screen.profile.ProfileScreen
import com.example.explory.ui.theme.Transparent
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Point
import com.mapbox.geojson.Polygon
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotationGroup
import com.mapbox.maps.extension.compose.style.StyleImage
import com.mapbox.maps.extension.compose.style.layers.generated.FillColor
import com.mapbox.maps.extension.compose.style.layers.generated.FillLayer
import com.mapbox.maps.extension.compose.style.layers.generated.FillOpacity
import com.mapbox.maps.extension.compose.style.layers.generated.FillPattern
import com.mapbox.maps.extension.compose.style.sources.generated.GeoJSONData
import com.mapbox.maps.extension.compose.style.sources.generated.GeoJsonSourceState
import com.mapbox.maps.extension.compose.style.standard.LightPreset
import com.mapbox.maps.extension.compose.style.standard.MapboxStandardStyle
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.annotation.AnnotationConfig
import com.mapbox.maps.plugin.annotation.AnnotationSourceOptions
import com.mapbox.maps.plugin.annotation.ClusterOptions
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
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
    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            center(Point.fromLngLat(0.0, 0.0))
            zoom(ZOOM)
            pitch(PITCH)
        }
    }

//    val latitude = mapState.userPosition?.first ?: 0.0
//    val longitude = mapState.userPosition?.second ?: 0.0


    val withHolesSourceState = remember {
        GeoJsonSourceState()
    }

    withHolesSourceState.data = GeoJSONData(
        Feature.fromGeometry(
            Polygon.fromOuterInner(viewModel.outerLineString, mapState.innerPoints.toList())
        )
    )

    // https://3djungle.ru/textures/ https://txtrs.ru/

    val painter = painterResource(id = R.drawable.cloud3)
    val imageBitmap: ImageBitmap = remember(painter) {
        painter.drawToImageBitmap()
    }

    val task = painterResource(id = R.drawable.marker)
    val taskBitmap: Bitmap = remember(task) {
        task.drawToImageBitmap().asAndroidBitmap()
    }

    val coin = painterResource(id = R.drawable.money)
    val coinBitmap: Bitmap = remember(task) {
        task.drawToImageBitmap().asAndroidBitmap()
    }


    Box(Modifier.fillMaxSize()) {
        RequestLocationPermission(
            requestCount = mapState.permissionRequestCount,
            onPermissionDenied = {
                scope.launch {
                    snackBarHostState.showSnackbar("You need to accept location permissions.")
                }
                viewModel.updateShowRequestPermissionButton(true)
            },
            onPermissionReady = {
                viewModel.updateShowRequestPermissionButton(false)
                viewModel.updateShowMap(true)
            }
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            RequestNotificationPermission()
        }

        if (mapState.showMap) {
            MapboxMap(
                Modifier.fillMaxSize(),
                scaleBar = {},
                logo = {},
                attribution = {},
                compass = {
                    Compass(
                        contentPadding = PaddingValues(20.dp),
                    )
                },
                style = {
                    MapboxStandardStyle(
                        topSlot = {
                            FillLayer(
                                sourceState = withHolesSourceState,
                                layerId = OPENED_WORLD_LAYER,
                                fillColor = FillColor(value = Color.DarkGray),
                                fillOpacity = FillOpacity(1.0),
                                fillPattern = FillPattern(StyleImage("fog", imageBitmap)),

                                )
                        },
                        lightPreset = LightPreset.NIGHT
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
                PointAnnotationGroup(
                    annotations = mapState.questPoints.map {
                        PointAnnotationOptions()
                            .withPoint(it)
                            .withIconImage(taskBitmap)
                            .withIconSize(1.0)
                    },
                    annotationConfig = AnnotationConfig(
                        annotationSourceOptions = AnnotationSourceOptions(
                            maxZoom = 10,
                            clusterOptions = ClusterOptions(
                                clusterMaxZoom = 20,
                                clusterRadius = 1,
                                colorLevels = listOf(
                                    Pair(0, Transparent.toArgb()),
                                )
                            )
                        )
                    ),
                    onClick = {
                        Toast.makeText(
                            context,
                            "Clicked on Point Annotation Cluster: $it",
                            Toast.LENGTH_SHORT
                        ).show()
                        true
                    }
                )

                PointAnnotationGroup(
                    annotations = mapState.coinPoints.map {
                        PointAnnotationOptions()
                            .withPoint(it)
                            .withIconImage(coinBitmap)
                            .withIconSize(1.0)
                    },
                    annotationConfig = AnnotationConfig(
                        annotationSourceOptions = AnnotationSourceOptions(
                            maxZoom = 10,
                            clusterOptions = ClusterOptions(
                                clusterMaxZoom = 20,
                                clusterRadius = 1,
                                colorLevels = listOf(
                                    Pair(0, Transparent.toArgb()),
                                )
                            )
                        )
                    )
                )

            }
        }

        if (mapState.showRequestPermissionButton) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.align(Alignment.Center)) {
                    Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = {
                            viewModel.incrementPermissionRequestCount()
                        }) {
                        Text("Request permission again (${mapState.permissionRequestCount})")
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

        TopInfoColumn(
            modifier = Modifier.align(Alignment.TopStart),
            currentLocationName = mapState.currentLocationName,
            currentLocationPercent = mapState.currentLocationPercent
        )
        SnackbarHost(snackBarHostState, modifier = Modifier.align(Alignment.BottomCenter))
        ButtonControlRow(
            mapViewportState = mapViewportState
        ) { viewModel.updateShowFriendScreen() }
    }

    if (mapState.showFriendsScreen) {
        ProfileScreen(
            state = 1,
            onBackClick = { viewModel.updateShowFriendScreen() },
            onInviteFriends = { },
            onSettingsClick = { },
        )
    }

}

@Composable
fun TopInfoColumn(
    modifier: Modifier = Modifier,
    currentLocationName: String = "Локация",
    currentLocationPercent: Double = 0.0,
) {
    Column(modifier = modifier) {
        Text(
            text = currentLocationName,
            color = Color.White,
        )
        Text(
            text = "$currentLocationPercent%",
            color = Color.White,
        )

    }
}


fun Painter.drawToImageBitmap(): ImageBitmap {
    val drawScope = CanvasDrawScope()
    val bitmap = ImageBitmap(intrinsicSize.width.toInt(), intrinsicSize.height.toInt())
    val canvas = Canvas(bitmap)
    drawScope.draw(
        density = Density(1f),
        layoutDirection = LayoutDirection.Rtl,
        canvas = canvas,
        size = intrinsicSize
    ) {
        draw(intrinsicSize)
    }
    return bitmap
}
