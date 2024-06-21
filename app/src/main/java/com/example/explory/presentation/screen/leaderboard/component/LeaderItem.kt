package com.example.explory.presentation.screen.leaderboard.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.explory.data.model.location.LocationStatisticDto
import com.example.explory.presentation.screen.map.component.Avatar

@Composable
fun LeaderItem(user: LocationStatisticDto) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ){
            Avatar(
                image = user.profileDto.avatarUrl,
                border = user.profileDto.inventoryDto.avatarFrames,
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column (
                modifier = Modifier
            ){
                Text(
                    text = user.profileDto.username,
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = user.profileDto.email,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "Уровень: " + user.level,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Расстояние: " + user.distance + " м",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Опыт: " + user.experience,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}