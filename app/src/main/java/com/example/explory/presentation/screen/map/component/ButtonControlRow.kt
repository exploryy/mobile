package com.example.explory.presentation.screen.map.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraEnhance
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.explory.R
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.plugin.viewport.ViewportStatus

@OptIn(MapboxExperimental::class)
@Composable
fun ButtonControlRow(
    mapViewportState: MapViewportState,
    onUpdateProfileScreenState: () -> Unit,
    onUpdateLeaderboardScreenState: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            if (mapViewportState.mapViewportStatus == ViewportStatus.Idle) {
                FloatingActionButton(
                    onClick = {
                        mapViewportState.transitionToFollowPuckState(
//                            followPuckViewportStateOptions = FollowPuckViewportStateOptions.Builder()
//                                .pitch(0.0).build(),
                        )

                    },
                    containerColor = Color.White,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(RoundedCornerShape(45))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.location),
                        contentDescription = "Locate button"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, bottom = 32.dp, top = 0.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FloatingActionButton(
                containerColor = Color.White,
                onClick = { onUpdateLeaderboardScreenState() },
                modifier = Modifier
                    .clip(CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.crown),
                    contentDescription = "Task button"
                )
            }

            FloatingActionButton(
                containerColor = Color.White,
                onClick = { onUpdateProfileScreenState() },
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.user),
                    contentDescription = "Profile button"
                )
            }
        }
    }
}
