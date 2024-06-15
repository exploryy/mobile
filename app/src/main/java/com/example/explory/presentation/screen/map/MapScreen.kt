package com.example.explory.presentation.screen.map

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.annotation.ColorInt
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.explory.R
import com.example.explory.data.model.quest.PointDto
import com.example.explory.presentation.screen.friendprofile.FriendProfileScreen
import com.example.explory.presentation.screen.map.component.ButtonControlRow
import com.example.explory.presentation.screen.map.component.EventDialog
import com.example.explory.presentation.screen.map.component.ShortQuestCard
import com.example.explory.presentation.screen.map.component.TopInfoColumn
import com.example.explory.presentation.screen.map.location.RequestLocationPermission
import com.example.explory.presentation.screen.map.notifications.RequestNotificationPermission
import com.example.explory.presentation.screen.profile.ProfileScreen
import com.example.explory.presentation.screen.quest.QuestSheet
import com.example.explory.presentation.screen.settings.SettingsScreen
import com.example.explory.presentation.screen.shop.ShopScreen
import com.example.explory.ui.theme.AccentColor
import com.example.explory.ui.theme.Black
import com.example.explory.ui.theme.Red
import com.example.explory.ui.theme.White
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Point
import com.mapbox.geojson.Polygon
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.ViewAnnotationAnchor
import com.mapbox.maps.dsl.cameraOptions
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
import com.mapbox.maps.extension.compose.style.layers.generated.LineColor
import com.mapbox.maps.extension.compose.style.layers.generated.LineLayer
import com.mapbox.maps.extension.compose.style.layers.generated.LineWidth
import com.mapbox.maps.extension.compose.style.sources.generated.GeoJSONData
import com.mapbox.maps.extension.compose.style.sources.generated.GeoJsonSourceState
import com.mapbox.maps.extension.compose.style.standard.MapboxStandardStyle
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions
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
//    themeViewModel: ThemeViewModel = koinViewModel(),
    onLogout: () -> Unit
) {
    val mapState by viewModel.mapState.collectAsStateWithLifecycle()
//    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
    LaunchedEffect(mapState.showMap) {
        if (mapState.showMap) {
            viewModel.startWebSockets()
        }
    }

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

    withHolesSourceState.data = GeoJSONData(
        Feature.fromGeometry(
            Polygon.fromOuterInner(viewModel.outerLineString, mapState.innerPoints.toList())
        )
    )

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
            },
            afterRequest = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    RequestNotificationPermission()
                }
            })

        if (mapState.showMap) {
            MapboxMap(Modifier.fillMaxSize(), onMapClickListener = {
                viewModel.updateShowViewAnnotationIndex(null)
                true
            }, scaleBar = {}, logo = {}, attribution = {}, compass = {
                Compass(
                    contentPadding = PaddingValues(vertical = 110.dp, horizontal = 20.dp),
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
                    },
//                        lightPreset = if (isDarkTheme) LightPreset.DUSK else LightPreset.DAY
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
//                        followPuckViewportStateOptions = FollowPuckViewportStateOptions.Builder()
//                            .pitch(PITCH).build(),
                    )

                }
                if (mapState.showViewAnnotationIndex != null) {
                    val quest = mapState.notCompletedQuests[mapState.showViewAnnotationIndex!!]
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
                mapState.notCompletedQuests.forEachIndexed { index, quest ->
                    val point =
                        Point.fromLngLat(quest.longitude.toDouble(), quest.latitude.toDouble())

                    PointAnnotation(point = point,
                        iconEmissiveStrength = 0.0,
                        iconHaloColorInt = Black.toArgb(),
                        iconHaloWidth = 1.0,
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
                            val coinsCount = mapState.coins.size
                            if (mapState.userPoint != null) {
                                viewModel.collectCoin(coin, mapState.userPoint!!)
                            }
//                            if (mapState.coins.size == coinsCount - 1) {
//                                Toast.makeText(context, "Монетка собрана!", LENGTH_SHORT).show()
//                            } else {
//                                Toast.makeText(context, "Вы слишком далеко", LENGTH_SHORT).show()
//                            }
                            true
                        })
                }

                mapState.friendsLocations.forEach { (userId, location) ->
                    val point = Point.fromLngLat(location.second, location.first)
                    val friendAvatar = mapState.friendAvatars[userId]?.second
                    val friendName = mapState.friendAvatars[userId]?.first

//                    val infiniteTransition = rememberInfiniteTransition(label = "")
//                    val animatedSize = infiniteTransition.animateFloat(
//                        initialValue = 0.9f,
//                        targetValue = 1.1f,
//                        animationSpec = infiniteRepeatable(
//                            animation = tween(durationMillis = 3000, easing = LinearEasing),
//                            repeatMode = RepeatMode.Reverse
//                        ), label = ""
//                    )

                    if (friendAvatar == null) {
                        val defaultAvatarBitmap = createDefaultAvatar(userId)
                        PointAnnotation(
                            point = point,
                            iconSize = 1.0,
                            iconImageBitmap = defaultAvatarBitmap,
                            onClick = {
                                viewModel.onFriendMarkerClicked(userId)
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
                                viewModel.onFriendMarkerClicked(userId)
                                true
                            },
                            textField = friendName,
                            textOffset = listOf(0.0, 2.0),
                            textColorInt = MaterialTheme.colorScheme.onSurface.toArgb()
                        )
                    }
                }

                if (mapState.p2pQuest != null) {
                    PolylineAnnotationGroup(lineDasharray = listOf(1.0, 1.0),
                        annotations = mutableListOf<PolylineAnnotationOptions>().apply {
                            val points = mapState.p2pQuest!!.route.points
                            points.forEachIndexed { index, pointDto ->
                                if (index < points.size - 1) {
                                    add(
                                        PolylineAnnotationOptions().withLineColor(AccentColor.toArgb())
                                            .withLineWidth(5.0).withPoints(
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
                            mapViewportState.flyTo(cameraOptions {
                                center(
                                    Point.fromLngLat(
                                        mapState.p2pQuest!!.route.points[points.size / 2].longitude.toDouble(),
                                        mapState.p2pQuest!!.route.points[points.size / 2].latitude.toDouble()
                                    )
                                )
                                zoom(13.0)
                                pitch(0.0)
                            }, animationOptions = MapAnimationOptions.mapAnimationOptions {
                                duration(2000)
                            })
                        })

                    PointAnnotation(
                        point = Point.fromLngLat(
                            mapState.p2pQuest!!.route.points.last().longitude.toDouble(),
                            mapState.p2pQuest!!.route.points.last().latitude.toDouble()
                        ),
                        iconSize = 1.0,
                        iconOffset = listOf(0.0, -20.0),
                        iconImageBitmap = finishBitmap
                    )
                }
                if (mapState.distanceQuest != null) {
                    val point = Point.fromLngLat(
                        mapState.distanceQuest!!.commonQuestDto.longitude.toDouble(),
                        mapState.distanceQuest!!.commonQuestDto.latitude.toDouble()
                    )
                    val points = viewModel.getPointsForCircle(
                        point.latitude(),
                        point.longitude(),
                        mapState.distanceQuest!!.distance.toDouble()
                    )
                    mapViewportState.flyTo(cameraOptions {
                        center(point)
                        zoom(12.0)
                        pitch(0.0)
                    }, animationOptions = MapAnimationOptions.mapAnimationOptions {
                        duration(2000)
                    })

                    Log.d("MapScreen", "points: $points")
                    PolygonAnnotation(
                        points = points,
                        fillColorInt = AccentColor.toArgb(),
                        fillOpacity = 0.7,
                        fillOutlineColorInt = White.toArgb(),
                    )
                }

                if (mapState.selectedFriendProfile != null) {
                    val userSourceState = GeoJsonSourceState(
                        initialData = GeoJSONData(
                            Feature.fromGeometry(
                                Polygon.fromLngLats(
                                    mapState.selectedFriendProfile!!.polygons
                                )
                            )
                        )
                    )
                    mapViewportState.flyTo(cameraOptions {
                        center(
                            Point.fromLngLat(
                                mapState.friendsLocations[mapState.selectedFriendProfile!!.id]!!.second,
                                mapState.friendsLocations[mapState.selectedFriendProfile!!.id]!!.first
                            )
                        )
                        zoom(14.0)
                        pitch(0.0)
                    }, animationOptions = MapAnimationOptions.mapAnimationOptions {
                        duration(2000)
                    })
                    FillLayer(
                        sourceState = userSourceState,
                        layerId = "user-layer",
                    )
                    LineLayer(
                        sourceState = userSourceState,
                        layerId = "user-line-layer",
                        lineColor = LineColor(Red),
                        lineWidth = LineWidth(5.0)
                    )
                }


            }
        }

        TopInfoColumn(
            modifier = Modifier.padding(vertical = 50.dp, horizontal = 20.dp),
            currentLocationName = mapState.currentLocationName,
            currentLocationPercent = mapState.currentLocationPercent,
            coinCount = mapState.balance
        )
        SnackbarHost(snackBarHostState, modifier = Modifier.align(Alignment.BottomCenter))
        Column(
            Modifier.fillMaxSize(), horizontalAlignment = Alignment.End
        ) {
            IconButton(
                onClick = { viewModel.updateShowSettingsScreen() },
                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .padding(top = 50.dp, bottom = 20.dp, start = 20.dp, end = 20.dp)
                    .clip(CircleShape)
                    .size(48.dp)

            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Settings",
                    tint = MaterialTheme.colorScheme.onPrimary
                )

            }

            IconButton(
                onClick = { viewModel.updateShopOpen() },
                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .padding(top = 50.dp, bottom = 20.dp, start = 20.dp, end = 20.dp)
                    .clip(CircleShape)
                    .size(48.dp)

            ) {
                Icon(
                    imageVector = Icons.Filled.Shop,
                    contentDescription = "Shop",
                    tint = MaterialTheme.colorScheme.onPrimary
                )

            }

            IconButton(
                onClick = {  },
                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .padding(top = 50.dp, bottom = 20.dp, start = 20.dp, end = 20.dp)
                    .clip(CircleShape)
                    .size(48.dp)

            ) {
                Icon(
                    imageVector = Icons.Filled.Backpack,
                    contentDescription = "Inventory",
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
                    .padding(start = 20.dp, top = 60.dp)
                    .size(45.dp)

            ) {
                Icon(
                    painter = painterResource(id = R.drawable.close),
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

        mapState.showRequestPermissionButton -> {
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

        mapState.p2pQuest != null -> {
            QuestSheet(name = mapState.p2pQuest!!.commonQuestDto.name,
                image = mapState.p2pQuest!!.commonQuestDto.images.first(),
                description = mapState.p2pQuest!!.commonQuestDto.description,
                difficulty = viewModel.getCorrectDifficulty(mapState.p2pQuest!!.commonQuestDto.difficultyType),
                transportType = viewModel.getCorrectTransportType(mapState.p2pQuest!!.commonQuestDto.transportType),
                distance = mapState.p2pQuest!!.route.distance,
                questStatus = if (mapState.activeQuest?.questId == mapState.p2pQuest!!.commonQuestDto.questId
                ) "активный" else null,
                point = mapState.p2pQuest!!.route.points.first(),
                onButtonClicked = {
                    if (mapState.activeQuest?.questId == mapState.p2pQuest!!.commonQuestDto.questId) {
                        viewModel.cancelQuest(mapState.p2pQuest!!.commonQuestDto.questId.toString())
                    } else {
                        viewModel.updateDistanceQuest(null)
                        viewModel.startQuest(mapState.p2pQuest!!.commonQuestDto.questId.toString())
                    }
                })
        }

        mapState.distanceQuest != null -> {
            QuestSheet(name = mapState.distanceQuest!!.commonQuestDto.name,
                image = mapState.distanceQuest!!.commonQuestDto.images.first(),
                description = mapState.distanceQuest!!.commonQuestDto.description,
                difficulty = viewModel.getCorrectDifficulty(mapState.distanceQuest!!.commonQuestDto.difficultyType),
                transportType = viewModel.getCorrectTransportType(mapState.distanceQuest!!.commonQuestDto.transportType),
                distance = mapState.distanceQuest!!.distance,
                questStatus = if (mapState.activeQuest?.questId == mapState.distanceQuest!!.commonQuestDto.questId
                ) "активный" else null,
                point = PointDto(
                    mapState.distanceQuest!!.commonQuestDto.longitude,
                    mapState.distanceQuest!!.commonQuestDto.latitude,
                    mapState.distanceQuest!!.commonQuestDto.longitude,
                    mapState.distanceQuest!!.commonQuestDto.latitude
                ),
                onButtonClicked = {
                    if (mapState.activeQuest?.questId == mapState.distanceQuest!!.commonQuestDto.questId) {
                        viewModel.cancelQuest(mapState.distanceQuest!!.commonQuestDto.questId.toString())
                    } else {
                        viewModel.updateP2PQuest(null)
                        viewModel.startQuest(mapState.distanceQuest!!.commonQuestDto.questId.toString())
                    }
                })
        }

        mapState.showSettingsScreen -> {
            SettingsScreen(onBackClick = { viewModel.updateShowSettingsScreen() })
        }

        mapState.selectedFriendProfile != null -> {
            FriendProfileScreen(friendId = mapState.selectedFriendProfile!!.id,
                onBackClick = { viewModel.closeFriendProfileScreen() })
        }

        mapState.isShopOpen -> {
            ShopScreen(onDismiss = { viewModel.updateShopOpen() })
        }
    }
    if (mapState.event != null) {
        EventDialog(event = mapState.event!!,
            onDismissRequest = { viewModel.updateEvent(null) },
            onFriendDecline = { viewModel.declineFriendRequest(it) },
            onFriendAccept = { viewModel.acceptFriendRequest(it) })
    }
    LaunchedEffect(mapState.toastText) {
        if (mapState.toastText != null) {
            Toast.makeText(context, mapState.toastText, LENGTH_SHORT).show()
            viewModel.updateToastText(null)
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
        defaultAvatarSize, defaultAvatarSize, Bitmap.Config.ARGB_8888
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

private fun drawableToBitmap(
    sourceDrawable: Drawable?,
    flipX: Boolean = false,
    flipY: Boolean = false,
    @ColorInt tint: Int? = null,
): Bitmap? {
    if (sourceDrawable == null) {
        return null
    }
    return if (sourceDrawable is BitmapDrawable) {
        sourceDrawable.bitmap
    } else {
        // copying drawable object to not manipulate on the same reference
        val constantState = sourceDrawable.constantState ?: return null
        val drawable = constantState.newDrawable().mutate()
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        tint?.let(drawable::setTint)
        val canvas = android.graphics.Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        canvas.scale(
            if (flipX) -1f else 1f, if (flipY) -1f else 1f, canvas.width / 2f, canvas.height / 2f
        )
        drawable.draw(canvas)
        bitmap
    }
}

private fun createCircularAvatar(bitmap: Bitmap, targetSize: Int): Bitmap {
    val scaledAvatarBitmap = Bitmap.createScaledBitmap(
        bitmap, targetSize, targetSize, false
    )

    val circleBitmap = Bitmap.createBitmap(
        scaledAvatarBitmap.width, scaledAvatarBitmap.height, Bitmap.Config.ARGB_8888
    )
    val canvas = android.graphics.Canvas(circleBitmap)
    val paint = Paint().apply {
        isAntiAlias = true
        shader = BitmapShader(
            scaledAvatarBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP
        )
    }
    val radius = scaledAvatarBitmap.width / 2f
    canvas.drawCircle(radius, radius, radius, paint)

    return circleBitmap
}