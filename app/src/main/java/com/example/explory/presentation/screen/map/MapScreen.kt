package com.example.explory.presentation.screen.map

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.explory.R
import com.example.explory.presentation.screen.map.component.ButtonControlRow
import com.example.explory.presentation.screen.map.component.ShortQuestCard
import com.example.explory.presentation.screen.map.component.TopInfoColumn
import com.example.explory.presentation.screen.map.location.RequestLocationPermission
import com.example.explory.presentation.screen.map.notifications.RequestNotificationPermission
import com.example.explory.presentation.screen.profile.ProfileScreen
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Point
import com.mapbox.geojson.Polygon
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.ViewAnnotationAnchor
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.ViewAnnotation
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.style.StyleImage
import com.mapbox.maps.extension.compose.style.layers.generated.FillAntialias
import com.mapbox.maps.extension.compose.style.layers.generated.FillLayer
import com.mapbox.maps.extension.compose.style.layers.generated.FillPattern
import com.mapbox.maps.extension.compose.style.sources.generated.GeoJSONData
import com.mapbox.maps.extension.compose.style.sources.generated.GeoJsonSourceState
import com.mapbox.maps.extension.compose.style.standard.LightPreset
import com.mapbox.maps.extension.compose.style.standard.MapboxStandardStyle
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.viewannotation.annotationAnchors
import com.mapbox.maps.viewannotation.geometry
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


const val ZOOM: Double = 0.0
const val PITCH: Double = 0.0
const val OPENED_WORLD_LAYER = "layer-parking"

@OptIn(MapboxExperimental::class)
@Composable
fun MapScreen(
    viewModel: MapViewModel = koinViewModel(),
    onLogout: () -> Unit,
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

    val painter = painterResource(id = R.drawable.snow2)
    val imageBitmap: ImageBitmap = remember(painter) {
        painter.drawToImageBitmap()
    }

    val task = painterResource(id = R.drawable.marker)
    val taskBitmap: Bitmap = remember(task) {
        task.drawToImageBitmap().asAndroidBitmap()
    }

    val coin = painterResource(id = R.drawable.money)
    val coinBitmap: Bitmap = remember(coin) {
        coin.drawToImageBitmap().asAndroidBitmap()
    }


    Box(Modifier.fillMaxSize()) {
        RequestLocationPermission(requestCount = mapState.permissionRequestCount,
            onPermissionDenied = {
                scope.launch {
                    snackBarHostState.showSnackbar("You need to accept location permissions.")
                }
                viewModel.updateShowRequestPermissionButton(true)
            },
            onPermissionReady = {
                viewModel.updateShowRequestPermissionButton(false)
                viewModel.updateShowMap(true)
            })
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            RequestNotificationPermission()
        }

        if (mapState.showMap) {
            MapboxMap(
                Modifier.fillMaxSize(),
                onMapClickListener = {
                    viewModel.updateShowViewAnnotationIndex(null)
                    true
                },
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
                                fillPattern = FillPattern(StyleImage("fog", imageBitmap)),
                                fillAntialias = FillAntialias(true),
                            )
                        }, lightPreset = LightPreset.default
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
                if (mapState.showViewAnnotationIndex != null) {
                    val quest = mapState.quests[mapState.showViewAnnotationIndex!!]
                    ViewAnnotation(options = viewAnnotationOptions {
                        geometry(
                            Point.fromLngLat(
                                quest.longitude.toDouble(), quest.latitude.toDouble()
                            )
                        )
                        allowOverlap(true)
                        ignoreCameraPadding(true)
                        allowOverlapWithPuck(true)
                        annotationAnchors(
                            {
                                anchor(ViewAnnotationAnchor.BOTTOM_LEFT)
                            }
                        )
                    }) {
                        ShortQuestCard(
                            questType = quest.questType,
                            difficultyColor = viewModel.getColorByDifficulty(quest.difficultyType),
                            name = quest.name
                        )
                    }
                }
                mapState.quests.forEachIndexed { index, quest ->
                    val point =
                        Point.fromLngLat(quest.longitude.toDouble(), quest.latitude.toDouble())

                    PointAnnotation(
                        point = point,
                        iconEmissiveStrength = 0.0,
                        iconImageBitmap = taskBitmap,
                        onClick = {
                            Log.d("MapScreen", "Click: $index")
                            viewModel.updateShowViewAnnotationIndex(index)
                            true
                        })
                }
//                PointAnnotationGroup(annotations = mapState.quests.map { quest ->
//                    val point =
//                        Point.fromLngLat(quest.longitude.toDouble(), quest.latitude.toDouble())
//                    PointAnnotationOptions().withPoint(point).withIconImage(taskBitmap)
//                        .withIconSize(1.0)
//                }, annotationConfig = AnnotationConfig(
//                    annotationSourceOptions = AnnotationSourceOptions(
//                        maxZoom = 10, clusterOptions = ClusterOptions(
//                            clusterMaxZoom = 20, clusterRadius = 1, colorLevels = listOf(
//                                Pair(0, Transparent.toArgb()),
//                            )
//                        )
//                    )
//                ), onClick = {
//                    viewModel.updateShowViewAnnotationIndex(mapState.quests.indexOf(it.point))
//                    true
//                })


                mapState.coins.forEach { coin ->
                    val point =
                        Point.fromLngLat(coin.longitude.toDouble(), coin.latitude.toDouble())
                    PointAnnotation(
                        point = point,
                        iconSize = 0.2,
                        iconEmissiveStrength = 0.0,
                        iconImageBitmap = coinBitmap,
                        onClick = {
                            Toast.makeText(context, "Это монетка", LENGTH_SHORT).show()
//                        Log.d("MapScreen", "Click: $index")
//                        viewModel.updateShowViewAnnotationIndex(index)
                            true
                        })
                }

//                PointAnnotationGroup(
//                    annotations = mapState.coins.map {
//                        val point =
//                            Point.fromLngLat(it.longitude.toDouble(), it.latitude.toDouble())
//                        Log.d("MapScreen", "MapScreen: ${it.longitude} ${it.latitude}")
//                        PointAnnotationOptions().withPoint(point).withIconImage(coinBitmap)
//                            .withIconSize(1.0)
//                    }, annotationConfig = AnnotationConfig(
//                        annotationSourceOptions = AnnotationSourceOptions(
//                            maxZoom = 10, clusterOptions = ClusterOptions(
//                                clusterMaxZoom = 20, clusterRadius = 1, colorLevels = listOf(
//                                    Pair(0, Transparent.toArgb()),
//                                )
//                            )
//                        )
//                    )
//                )

            }
        }

        if (mapState.showRequestPermissionButton) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.align(Alignment.Center)) {
                    Button(modifier = Modifier.align(Alignment.CenterHorizontally), onClick = {
                        viewModel.incrementPermissionRequestCount()
                    }) {
                        Text("Request permission again (${mapState.permissionRequestCount})")
                    }
                    Button(modifier = Modifier.align(Alignment.CenterHorizontally), onClick = {
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
            onBackClick = { viewModel.updateShowFriendScreen() },
            onLogout = { onLogout() },
            onInviteFriends = { },
            onSettingsClick = { },
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
