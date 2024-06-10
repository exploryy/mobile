package com.example.explory.presentation.screen.map

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.Shader
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.explory.R
import com.example.explory.data.service.PointDto
import com.example.explory.presentation.screen.common.ThemeViewModel
import com.example.explory.presentation.screen.map.component.ButtonControlRow
import com.example.explory.presentation.screen.map.component.ShortQuestCard
import com.example.explory.presentation.screen.map.component.TopInfoColumn
import com.example.explory.presentation.screen.map.location.RequestLocationPermission
import com.example.explory.presentation.screen.map.notifications.RequestNotificationPermission
import com.example.explory.presentation.screen.profile.ProfileScreen
import com.example.explory.presentation.screen.quest.QuestSheet
import com.example.explory.presentation.screen.settings.SettingsScreen
import com.example.explory.ui.theme.AccentColor
import com.example.explory.ui.theme.Black
import com.example.explory.ui.theme.White
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
import com.mapbox.maps.extension.compose.annotation.generated.PolygonAnnotation
import com.mapbox.maps.extension.compose.annotation.generated.PolylineAnnotationGroup
import com.mapbox.maps.extension.compose.style.layers.generated.FillAntialias
import com.mapbox.maps.extension.compose.style.layers.generated.FillColor
import com.mapbox.maps.extension.compose.style.layers.generated.FillLayer
import com.mapbox.maps.extension.compose.style.sources.generated.GeoJSONData
import com.mapbox.maps.extension.compose.style.sources.generated.GeoJsonSourceState
import com.mapbox.maps.extension.compose.style.standard.LightPreset
import com.mapbox.maps.extension.compose.style.standard.MapboxStandardStyle
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.viewport.data.FollowPuckViewportStateOptions
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
    themeViewModel: ThemeViewModel = koinViewModel(),
    onLogout: () -> Unit
) {
    val mapState by viewModel.mapState.collectAsStateWithLifecycle()
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            zoom(ZOOM)
            pitch(PITCH)
        }
    }


    val withHolesSourceState = remember {
        GeoJsonSourceState()
    }

//    withHolesSourceState.data = GeoJSONData(
//        Feature.fromGeometry(
//            Polygon.fromOuterInner(viewModel.outerLineString, mapState.innerPoints.toList())
//        )
//    )

    // https://3djungle.ru/textures/ https://txtrs.ru/

//    val painter = painterResource(id = R.drawable.snow2)
//    val imageBitmap: ImageBitmap = remember(painter) {
//        painter.drawToImageBitmap()
//    }

    val task = painterResource(id = R.drawable.marker)
    val taskBitmap: Bitmap = remember(task) {
        task.drawToImageBitmap().asAndroidBitmap()
    }

    val finish = painterResource(id = R.drawable.finish)
    val finishBitmap: Bitmap = remember(finish) {
        finish.drawToImageBitmap().asAndroidBitmap()
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
            MapboxMap(Modifier.fillMaxSize(), onMapClickListener = {
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
                }, style = {
                    MapboxStandardStyle(
                        topSlot = {
                            FillLayer(
                                sourceState = withHolesSourceState,
                                layerId = OPENED_WORLD_LAYER,
                                fillColor = FillColor(Color(0xFF000000)),
//                                fillPattern = FillPattern(StyleImage("fog", imageBitmap)),
                                fillAntialias = FillAntialias(true),
                            )
                        }, lightPreset = if (isDarkTheme) LightPreset.DUSK else LightPreset.DAY
                    )
                }, mapViewportState = mapViewportState
            ) {
                MapEffect(Unit) { mapView ->
                    mapView.location.updateSettings {
                        locationPuck = createDefault2DPuck(withBearing = true)
                        puckBearingEnabled = true
                        puckBearing = PuckBearing.HEADING
                        enabled = true
                    }
                    mapViewportState.transitionToFollowPuckState(
                        followPuckViewportStateOptions = FollowPuckViewportStateOptions.Builder()
                            .pitch(PITCH).build(),
                    )

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
                        annotationAnchors({
                            anchor(ViewAnnotationAnchor.BOTTOM_LEFT)
                        })
                    }) {
                        ShortQuestCard(questType = viewModel.getNameByType(quest.questType),
                            difficultyColor = viewModel.getColorByDifficulty(quest.difficultyType),
                            name = quest.name,
                            onDetailsClick = {
                                viewModel.updateShowViewAnnotationIndex(null)
                                viewModel.getQuestDetails(quest.questId.toString(), quest.questType)
                            })
                    }
                }
                mapState.quests.forEachIndexed { index, quest ->
                    val point =
                        Point.fromLngLat(quest.longitude.toDouble(), quest.latitude.toDouble())

                    PointAnnotation(point = point,
                        iconEmissiveStrength = 0.0,
                        iconImageBitmap = taskBitmap,
                        onClick = {
                            viewModel.updateShowViewAnnotationIndex(index)
                            true
                        })
                }

                mapState.coins.forEach { coin ->
                    val point =
                        Point.fromLngLat(coin.longitude.toDouble(), coin.latitude.toDouble())
                    PointAnnotation(point = point,
                        iconSize = 0.2,
                        iconEmissiveStrength = 0.0,
                        iconImageBitmap = coinBitmap,
                        onClick = {
                            Toast.makeText(context, "Это монетка =)", LENGTH_SHORT).show()
                            true
                        })
                }

                if (mapState.p2pQuest != null) {
                    PolylineAnnotationGroup(annotations = mutableListOf<PolylineAnnotationOptions>().apply {
                        val points = mapState.p2pQuest!!.route.points
                        points.forEachIndexed { index, pointDto ->
                            if (index < points.size - 1) {
                                add(
                                    PolylineAnnotationOptions().withLineColor(AccentColor.toArgb())
                                        .withLineWidth(3.0)
                                        .withPoints(
                                            listOf(
                                                Point.fromLngLat(
                                                    pointDto.longitude.toDouble(),
                                                    pointDto.latitude.toDouble()
                                                ), Point.fromLngLat(
                                                    points[index + 1].longitude.toDouble(),
                                                    points[index + 1].latitude.toDouble()
                                                )
                                            )
                                        )
                                )
                            }
                        }
                    })
                    PointAnnotation(point = Point.fromLngLat(
                        mapState.p2pQuest!!.route.points.last().longitude.toDouble(),
                        mapState.p2pQuest!!.route.points.last().latitude.toDouble()
                    ),
                        iconEmissiveStrength = 0.0,
                        iconSize = 1.0,
                        iconImageBitmap = finishBitmap,
                        onClick = {
                            true
                        }
                    )
                }
                if (mapState.distanceQuest != null) {
                    // draw cycle with radius = distance in meters from center point
                    val points = viewModel.getPointsForCircle(
                        mapState.distanceQuest!!.commonQuestDto.latitude.toDouble(),
                        mapState.distanceQuest!!.commonQuestDto.longitude.toDouble(),
                        mapState.distanceQuest!!.distance.toDouble()
                    )
                    Log.d("MapScreen", "points: $points")
                    PolygonAnnotation(
                        points = points,
                        fillColorInt = AccentColor.toArgb(),
                        fillOpacity = 0.5,
                        fillOutlineColorInt = White.toArgb()
                    )
                }

                mapState.friendsLocations.forEach { (userId, location) ->
                    val point = Point.fromLngLat(location.second, location.first)
                    val friendAvatar = mapState.friendAvatars[userId]?.second
                    val friendName = mapState.friendAvatars[userId]?.first

                    if (friendAvatar == null){
                        val defaultAvatarBitmap = createDefaultAvatar(userId)
                        PointAnnotation(
                            point = point,
                            iconSize = 1.0,
                            iconImageBitmap = defaultAvatarBitmap,
                            onClick = {
                                Toast.makeText(context, "Друг: $userId", LENGTH_SHORT).show()
                                true
                            },
                            textField = friendName,
                            textOffset = listOf(0.0, 2.0)
                        )

                    } else {
                        val targetSize = 100
                        val circleBitmap = createCircularAvatar(friendAvatar, targetSize)

                        PointAnnotation(
                            point = point,
                            iconSize = 1.0,
                            iconImageBitmap = circleBitmap,
                            onClick = {
                                Toast.makeText(context, "Друг: $userId", LENGTH_SHORT).show()
                                true
                            },
                            textField = friendName,
                            textOffset = listOf(0.0, 2.0),
                            textColorInt = MaterialTheme.colorScheme.onSurface.toArgb()
                        )
                    }
                }
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
            modifier = Modifier.padding(20.dp),
            currentLocationName = mapState.currentLocationName,
            currentLocationPercent = mapState.currentLocationPercent
        )
        SnackbarHost(snackBarHostState, modifier = Modifier.align(Alignment.BottomCenter))
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 80.dp, bottom = 20.dp, start = 20.dp, end = 20.dp)
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            IconButton(
                onClick = { viewModel.updateShowSettingsScreen() },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Settings",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        ButtonControlRow(
            mapViewportState = mapViewportState
        ) { viewModel.updateShowFriendScreen() }
        if (mapState.p2pQuest != null || mapState.distanceQuest != null) {
            IconButton(
                colors = IconButtonDefaults.iconButtonColors(containerColor = Black), onClick = {
                    viewModel.updateP2PQuest(null)
                    viewModel.updateDistanceQuest(null)
                }, modifier = Modifier
                    .padding(20.dp)
                    .size(45.dp)

            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    tint = Color.White,
                    contentDescription = null,
                    modifier = Modifier.scale(1.4f)
                )
            }
        }
    }

    when {
        mapState.showFriendsScreen -> {
            ProfileScreen(
                onBackClick = { viewModel.updateShowFriendScreen() },
                onLogout = { onLogout() },
            )
        }

        mapState.p2pQuest != null -> {
            QuestSheet(
                name = mapState.p2pQuest!!.commonQuestDto.name,
                image = mapState.p2pQuest!!.commonQuestDto.images.first(),
                description = mapState.p2pQuest!!.commonQuestDto.description,
                difficulty = viewModel.getCorrectDifficulty(mapState.p2pQuest!!.commonQuestDto.difficultyType),
                transportType = viewModel.getCorrectTransportType(mapState.p2pQuest!!.commonQuestDto.transportType),
                distance = mapState.p2pQuest!!.route.distance,
                point = mapState.p2pQuest!!.route.points.first()
            )
        }

        mapState.showSettingsScreen -> {
            SettingsScreen(
                onBackClick = { viewModel.updateShowSettingsScreen() }
            )
        }

        mapState.distanceQuest != null -> {
            QuestSheet(
                name = mapState.distanceQuest!!.commonQuestDto.name,
                image = mapState.distanceQuest!!.commonQuestDto.images.first(),
                description = mapState.distanceQuest!!.commonQuestDto.description,
                difficulty = viewModel.getCorrectDifficulty(mapState.distanceQuest!!.commonQuestDto.difficultyType),
                transportType = viewModel.getCorrectTransportType(mapState.distanceQuest!!.commonQuestDto.transportType),
                distance = mapState.distanceQuest!!.distance,
                point = PointDto(
                    mapState.distanceQuest!!.commonQuestDto.longitude,
                    mapState.distanceQuest!!.commonQuestDto.latitude,
                    mapState.distanceQuest!!.commonQuestDto.longitude,
                    mapState.distanceQuest!!.commonQuestDto.latitude
                )
            )
        }
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

private fun createDefaultAvatar(userId: String): Bitmap {
    val defaultAvatarSize = 100
    val defaultAvatarBitmap = Bitmap.createBitmap(
        defaultAvatarSize,
        defaultAvatarSize,
        Bitmap.Config.ARGB_8888
    )
    val canvas = android.graphics.Canvas(defaultAvatarBitmap)
    val paint = Paint().apply {
        isAntiAlias = true
        color = android.graphics.Color.GRAY
    }
    val radius = defaultAvatarSize / 2f
    canvas.drawCircle(radius, radius, radius, paint)

    return defaultAvatarBitmap
}

private fun createCircularAvatar(bitmap: Bitmap, targetSize: Int): Bitmap {
    val scaledAvatarBitmap = Bitmap.createScaledBitmap(
        bitmap,
        targetSize,
        targetSize,
        false
    )

    val circleBitmap = Bitmap.createBitmap(
        scaledAvatarBitmap.width,
        scaledAvatarBitmap.height,
        Bitmap.Config.ARGB_8888
    )
    val canvas = android.graphics.Canvas(circleBitmap)
    val paint = Paint().apply {
        isAntiAlias = true
        shader = BitmapShader(
            scaledAvatarBitmap,
            Shader.TileMode.CLAMP,
            Shader.TileMode.CLAMP
        )
    }
    val radius = scaledAvatarBitmap.width / 2f
    canvas.drawCircle(radius, radius, radius, paint)

    return circleBitmap
}