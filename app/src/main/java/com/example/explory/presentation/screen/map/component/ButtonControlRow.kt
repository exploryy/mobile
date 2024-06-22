package com.example.explory.presentation.screen.map.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            if (mapViewportState.mapViewportStatus == ViewportStatus.Idle) {
                MapBigButton(
                    modifier = Modifier.size(70.dp),
                    shape = RoundedCornerShape(45),
                    onClick = {
                        mapViewportState.transitionToFollowPuckState()
                    },
                    icon = R.drawable.location,
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, bottom = 32.dp, top = 0.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MapBigButton(
                modifier = Modifier.size(60.dp),
                shape = RoundedCornerShape(45),
                onClick = onUpdateLeaderboardScreenState,
                icon = R.drawable.crown,
                tint = MaterialTheme.colorScheme.primary
            )
            MapBigButton(
                modifier = Modifier.size(60.dp),
                shape = RoundedCornerShape(45),
                onClick = onUpdateProfileScreenState,
                icon = R.drawable.user,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

