package com.example.explory.presentation.map.component

import android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.plugin.viewport.ViewportStatus

@OptIn(MapboxExperimental::class)
@Composable
fun ButtonControlRow(
    mapViewportState: MapViewportState,
    onUpdateFriendScreenState: () -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FloatingActionButton(
                onClick = { onUpdateFriendScreenState() },
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(25))
            ) {
                Text("Друзья")
            }

            FloatingActionButton(
                onClick = {  },
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(25))
            ) {
                Text("Задания")
            }

            FloatingActionButton(
                onClick = {  },
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(25))
            ) {
                Text("Достижения")
            }
        }
    }
}
