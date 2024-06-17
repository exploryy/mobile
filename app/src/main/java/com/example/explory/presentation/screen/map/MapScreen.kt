package com.example.explory.presentation.screen.map

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.explory.R
import com.example.explory.data.model.quest.PointDto
import com.example.explory.presentation.screen.friendprofile.FriendProfileScreen
import com.example.explory.presentation.screen.inventory.InventoryScreen
import com.example.explory.presentation.screen.map.component.ButtonControlRow
import com.example.explory.presentation.screen.map.component.ErrorScreen
import com.example.explory.presentation.screen.map.component.EventDialog
import com.example.explory.presentation.screen.map.component.LoadingScreen
import com.example.explory.presentation.screen.map.component.RequestPermissionsScreen
import com.example.explory.presentation.screen.map.component.ShortQuestCard
import com.example.explory.presentation.screen.map.component.TopInfoColumn
import com.example.explory.presentation.screen.map.location.RequestLocationPermission
import com.example.explory.presentation.screen.map.notifications.RequestNotificationPermission
import com.example.explory.presentation.screen.profile.ProfileScreen
import com.example.explory.presentation.screen.quest.QuestSheet
import com.example.explory.presentation.screen.settings.SettingsScreen
import com.example.explory.presentation.screen.shop.ShopScreen
import com.example.explory.presentation.utils.UiState
import com.example.explory.ui.theme.AccentColor
import com.example.explory.ui.theme.Black
import com.example.explory.ui.theme.Red
import com.example.explory.ui.theme.Transparent
import com.example.explory.ui.theme.White
import com.mapbox.geojson.Feature
import com.mapbox.geojson.LineString
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
import com.mapbox.maps.extension.compose.style.layers.generated.FillColor
import com.mapbox.maps.extension.compose.style.layers.generated.FillLayer
import com.mapbox.maps.extension.compose.style.layers.generated.FillPattern
import com.mapbox.maps.extension.compose.style.layers.generated.LineBorderColor
import com.mapbox.maps.extension.compose.style.layers.generated.LineBorderWidth
import com.mapbox.maps.extension.compose.style.layers.generated.LineCap
import com.mapbox.maps.extension.compose.style.layers.generated.LineColor
import com.mapbox.maps.extension.compose.style.layers.generated.LineJoin
import com.mapbox.maps.extension.compose.style.layers.generated.LineLayer
import com.mapbox.maps.extension.compose.style.layers.generated.LineWidth
import com.mapbox.maps.extension.compose.style.sources.generated.GeoJSONData
import com.mapbox.maps.extension.compose.style.sources.generated.GeoJsonSourceState
import com.mapbox.maps.extension.compose.style.standard.LightPreset
import com.mapbox.maps.extension.compose.style.standard.MapboxStandardStyle
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.viewport.data.DefaultViewportTransitionOptions
import com.mapbox.maps.viewannotation.annotationAnchors
import com.mapbox.maps.viewannotation.geometry
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import org.koin.androidx.compose.koinViewModel


const val ZOOM: Double = 0.0
const val PITCH: Double = 0.0
const val OPENED_WORLD_LAYER = "opened-layer"

@OptIn(MapboxExperimental::class)
@Composable
fun MapScreen(
    viewModel: MapViewModel = koinViewModel(), onLogout: () -> Unit
) {
    val mapState by viewModel.mapState.collectAsStateWithLifecycle()
    LaunchedEffect(mapState.uiState) {
        Log.d("MapScreen", "LaunchedEffect now state is ${mapState.uiState}")
        if (mapState.uiState == UiState.PermissionGranted) {
            Log.d("MapScreen", "StartData")
            viewModel.getStartData()
        }
    }

    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            zoom(ZOOM)
            pitch(PITCH)
        }
    }

    val withHolesSourceState = remember { GeoJsonSourceState() }

    withHolesSourceState.data = GeoJSONData(
        Feature.fromGeometry(
            Polygon.fromOuterInner(viewModel.outerLineString, mapState.innerPoints.toList())
        )
    )


    // https://3djungle.ru/textures/ https://txtrs.ru/


    val taskBitmap: Bitmap? = remember {
        drawableToBitmap(AppCompatResources.getDrawable(context, R.drawable.marker))
    }

    val finishBitmap: Bitmap? = remember {
        drawableToBitmap(AppCompatResources.getDrawable(context, R.drawable.finish))
    }

    val coinBitmap: Bitmap? = remember {
        drawableToBitmap(AppCompatResources.getDrawable(context, R.drawable.money))
    }

    val fogGrass = painterResource(id = R.drawable.grass)
    val fogGrassBitmap: ImageBitmap = remember(fogGrass) {
        fogGrass.drawToImageBitmap()
    }

//    val fogBitmap = viewModel.loadImage(mapState.currentUserFog?.url)
//    val fogImageBitmap: ImageBitmap? = remember(fogBitmap) {
//        fogBitmap?.asImageBitmap()
//    }

    val fogWater = painterResource(id = R.drawable.water)
    val fogWaterBitmap: ImageBitmap = remember(fogWater) {
        fogWater.drawToImageBitmap()
    }

    val fogSnow = painterResource(id = R.drawable.snow)
    val fogSnowBitmap: ImageBitmap = remember(fogSnow) {
        fogSnow.drawToImageBitmap()
    }

    val fogCloud = painterResource(id = R.drawable.cloud3)
    val fogCloudBitmap: ImageBitmap = remember(fogCloud) {
        fogCloud.drawToImageBitmap()
    }


    Box(Modifier.fillMaxSize()) {
        RequestLocationPermission(requestCount = mapState.permissionRequestCount,
            onPermissionDenied = {
                viewModel.updateToastText("You need to accept location permissions")
                viewModel.updateUiState(UiState.NoPermissions)
            },
            onPermissionReady = {
                Log.d("MapScreen", "PermissionReady")
                viewModel.updateUiState(UiState.PermissionGranted)
            },
            afterRequest = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    RequestNotificationPermission()
                }
            })

        when (mapState.uiState) {
            is UiState.Default -> {
                MapboxMap(Modifier.fillMaxSize(), onMapClickListener = {
                    viewModel.updateShowViewAnnotationIndex(null)
                    true
                }, scaleBar = {}, logo = {}, attribution = {}, compass = {
                    Compass(
                        contentPadding = PaddingValues(vertical = 120.dp, horizontal = 20.dp),
                    )
                }, style = {
                    MapboxStandardStyle(
                        topSlot = {
                            fun getImageBitmapById(id: Long): ImageBitmap {
                                return when (id) {
                                    2L -> fogGrassBitmap
                                    5L -> fogWaterBitmap
                                    6L -> fogCloudBitmap
                                    7L -> fogSnowBitmap
                                    else -> fogGrassBitmap
                                }
                            }

                            FillLayer(
                                sourceState = withHolesSourceState,
                                layerId = OPENED_WORLD_LAYER,
                                fillColor = FillColor(Black),
                                fillPattern = if (mapState.currentUserFog == null) FillPattern.default else

                                    FillPattern(
                                        StyleImage(
                                            "fog",
                                            getImageBitmapById(mapState.currentUserFog!!.itemId)
                                        )
                                    ),
                                fillAntialias = FillAntialias(true),
                            )
                        },
                        lightPreset = if (mapState.isDarkTheme) LightPreset.DUSK else LightPreset.DAY
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
                            defaultTransitionOptions = DefaultViewportTransitionOptions.Builder()
                                .maxDurationMs(0).build()
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
                                    viewModel.getQuestDetails(
                                        quest.questId.toString(), quest.questType
                                    )
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
                                if (mapState.userPoint != null) {
                                    viewModel.collectCoin(coin, mapState.userPoint!!)
                                }
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

                        PointAnnotation(
                            point = point,
                            iconSize = 1.0,
                            iconImageBitmap = if (friendAvatar != null) createCircularAvatar(
                                friendAvatar, 100
                            ) else createDefaultAvatar(),
                            onClick = {
                                viewModel.onFriendMarkerClicked(userId)
                                true
                            },
                            textField = friendName,
                            textOffset = listOf(0.0, 2.0),
                            textColorInt = MaterialTheme.colorScheme.onSurface.toArgb()
                        )
                    }

                    if (mapState.p2pQuest != null) {
                        val lineSourceData =
                            GeoJSONData(Feature.fromGeometry(LineString.fromLngLats(mapState.p2pQuest!!.route.points.map {
                                Point.fromLngLat(
                                    it.longitude.toDouble(), it.latitude.toDouble()
                                )
                            })))
                        LineLayer(
                            sourceState = GeoJsonSourceState(initialData = lineSourceData),
                            layerId = "line-layer",
                            lineColor = LineColor(AccentColor),
                            lineBorderColor = LineBorderColor(White),
                            lineJoin = LineJoin.ROUND,
                            lineCap = LineCap.ROUND,
                            lineBorderWidth = LineBorderWidth(3.0),
                            lineWidth = LineWidth(10.0)
                        )
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
                            point.latitude(), point.longitude(), mapState.distanceQuest!!.distance
                        )

                        val circleSourceState = GeoJsonSourceState(
                            initialData = GeoJSONData(
                                Feature.fromGeometry(
                                    Polygon.fromLngLats(
                                        points
                                    )
                                )
                            )
                        )
                        FillLayer(
                            sourceState = circleSourceState,
                            fillColor = FillColor(Transparent),
                            layerId = "circle-layer",
                        )
                        LineLayer(
                            sourceState = circleSourceState,
                            layerId = "circle-line-layer",
                            lineColor = LineColor(AccentColor),
                            lineWidth = LineWidth(5.0)
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
                        FillLayer(
                            sourceState = userSourceState,
                            fillColor = FillColor(Transparent),
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

                Column {
                    TopInfoColumn(
                        modifier = Modifier.padding(top = 50.dp, start = 20.dp, end = 20.dp),
                        currentLocationName = mapState.currentLocationName,
                        currentLocationPercent = mapState.currentLocationPercent,
                        coinCount = mapState.userBalance?.balance ?: 0,
                        currentLevel = mapState.userBalance?.level ?: 0,
                        exp = mapState.userBalance?.experience ?: 0,
                        expToNextLevel = mapState.userBalance?.totalExperienceInLevel ?: 100
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(
                        Modifier.padding(start = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        IconButton(
                            onClick = { viewModel.updateShowSettingsScreen() },
                            colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier
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
                            onClick = { viewModel.updateInventoryOpenScreen() },
                            colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier
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
                }

                ButtonControlRow(
                    mapViewportState = mapViewportState
                ) { viewModel.updateShowFriendScreen() }
                Log.d("MapScreen", "Quests ${mapState.p2pQuest} ${mapState.distanceQuest}")
                if (mapState.p2pQuest != null || mapState.distanceQuest != null) {
                    Log.d("MapScreen", "Quests is opened")
                    IconButton(
                        colors = IconButtonDefaults.iconButtonColors(containerColor = Black),
                        onClick = {
                            viewModel.updateP2PQuest(null)
                            viewModel.updateDistanceQuest(null)
                        },
                        modifier = Modifier
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

            is UiState.Error -> {
                ErrorScreen(message = (mapState.uiState as UiState.Error).message)
            }

            UiState.Loading, UiState.PermissionGranted -> LoadingScreen()
            UiState.NoPermissions -> RequestPermissionsScreen(
                onRequestAgain = {
                    viewModel.incrementPermissionRequestCount()
                },
                onGoToSettings = {
                    context.startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", context.packageName, null)
                        )
                    )
                }
            )
        }
        SnackbarHost(snackBarHostState, modifier = Modifier.align(Alignment.BottomCenter))
    }

    when {
        mapState.showFriendsScreen -> {
            ProfileScreen(
                onBackClick = { viewModel.updateShowFriendScreen() },
                onLogout = { onLogout() },
            )
        }


        mapState.p2pQuest != null -> {
            val imageUrl = if (mapState.p2pQuest!!.commonQuestDto.images.isEmpty()) null
            else mapState.p2pQuest!!.commonQuestDto.images.first()

            QuestSheet(name = mapState.p2pQuest!!.commonQuestDto.name,
                image = imageUrl,
                description = mapState.p2pQuest!!.commonQuestDto.description,
                difficulty = viewModel.getCorrectDifficulty(mapState.p2pQuest!!.commonQuestDto.difficultyType),
                transportType = viewModel.getCorrectTransportType(mapState.p2pQuest!!.commonQuestDto.transportType),
                distance = mapState.p2pQuest!!.route.distance,
                questStatus = if (mapState.activeQuest?.questId == mapState.p2pQuest!!.commonQuestDto.questId) "активный" else null,
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
            val imageUrl = if (mapState.distanceQuest!!.commonQuestDto.images.isEmpty()) null
            else mapState.distanceQuest!!.commonQuestDto.images.first()

            QuestSheet(name = mapState.distanceQuest!!.commonQuestDto.name,
                image = imageUrl,
                description = mapState.distanceQuest!!.commonQuestDto.description,
                difficulty = viewModel.getCorrectDifficulty(mapState.distanceQuest!!.commonQuestDto.difficultyType),
                transportType = viewModel.getCorrectTransportType(mapState.distanceQuest!!.commonQuestDto.transportType),
                distance = mapState.distanceQuest!!.distance,
                questStatus = if (mapState.activeQuest?.questId == mapState.distanceQuest!!.commonQuestDto.questId) "активный" else null,
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
            SettingsScreen(
                isDarkTheme = mapState.isDarkTheme,
                onThemeChangeClick = { isDarkTheme ->
                    viewModel.updateTheme(isDarkTheme)
                },
                onBackClick = { viewModel.updateShowSettingsScreen() })
        }

        mapState.selectedFriendProfile != null -> {
            FriendProfileScreen(friendId = mapState.selectedFriendProfile!!.id,
                onBackClick = { viewModel.closeFriendProfileScreen() })
        }

        mapState.isShopOpen -> {
            ShopScreen(onDismiss = { viewModel.updateShopOpen() })
        }

        mapState.isInventoryOpen -> {
            InventoryScreen(onDismiss = { viewModel.updateInventoryOpenScreen() })
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
//            Toast.makeText(context, mapState.toastText, LENGTH_SHORT).show()
            snackBarHostState.showSnackbar(mapState.toastText!!)
            viewModel.updateToastText(null)
        }
    }

}


