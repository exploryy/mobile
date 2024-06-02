package com.example.explory.presentation.screen.map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.explory.R
import com.example.explory.data.model.friend.Friend
import com.example.explory.presentation.screen.map.component.ButtonControlRow
import com.example.explory.presentation.screen.map.location.RequestLocationPermission
import com.example.explory.presentation.screen.map.notifications.RequestNotificationPermission
import com.example.explory.presentation.screen.profile.ProfileScreen
import com.mapbox.geojson.Feature
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
import com.mapbox.maps.extension.compose.style.standard.LightPreset
import com.mapbox.maps.extension.compose.style.standard.MapboxStandardStyle
import com.mapbox.maps.plugin.PuckBearing
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

    val withHolesSourceState = remember {
        GeoJsonSourceState()
    }

    withHolesSourceState.data = GeoJSONData(
        Feature.fromGeometry(
            Polygon.fromOuterInner(viewModel.outerLineString, mapState.innerPoints.toList())
        )
    )

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
                style = {
                    MapboxStandardStyle(
                        topSlot = {
                            FillLayer(
                                sourceState = withHolesSourceState,
                                layerId = OPENED_WORLD_LAYER,
                                fillColor = FillColor(Color.DarkGray),
                                fillOpacity = FillOpacity(1.0)
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
            friends = friends
        )
    }

}

val friends = listOf(
    Friend("fedosssssss", "3 км", R.drawable.picture, isBestFriend = true),
    Friend("vag55", "3 км", R.drawable.picture, isBestFriend = true),
    Friend("сахарок", "3 км", R.drawable.picture, isBestFriend = false),
    Friend("liiid", "давно не видели", R.drawable.picture, isBestFriend = false),
    Friend("fedosssssss", "3 км", R.drawable.picture, isBestFriend = false),
    Friend("vag55", "3 км", R.drawable.picture, isBestFriend = false),
    Friend("сахарок", "3 км", R.drawable.picture, isBestFriend = false),
    Friend("liiid", "давно не видели", R.drawable.picture, isBestFriend = false),
    Friend("fedosssssss", "3 км", R.drawable.picture, isBestFriend = false),
    Friend("vag55", "3 км", R.drawable.picture, isBestFriend = false),
    Friend("сахарок", "3 км", R.drawable.picture, isBestFriend = false),
    Friend("liiid", "давно не видели", R.drawable.picture, isBestFriend = false),
    Friend("fedosssssss", "3 км", R.drawable.picture, isBestFriend = false),
    Friend("vag55", "3 км", R.drawable.picture, isBestFriend = false),
    Friend("сахарок", "3 км", R.drawable.picture, isBestFriend = false),
    Friend("liiid", "давно не видели", R.drawable.picture, isBestFriend = false),
)

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun Context.checkAndRequestNotificationPermission(
    launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>,
) {
    val notificationPermission = arrayOf(
        Manifest.permission.ACCESS_NOTIFICATION_POLICY,
        Manifest.permission.POST_NOTIFICATIONS,
    )
    if (notificationPermission.all {
            ContextCompat.checkSelfPermission(
                this,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    ) {
        return
    }
    launcher.launch(notificationPermission)
}