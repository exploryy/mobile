package com.example.explory.presentation.screen.leaderboard.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.explory.R
import com.example.explory.data.model.location.LocationStatisticDto
import com.example.explory.presentation.screen.map.component.Avatar
import com.example.explory.presentation.screen.userstatistic.getDistanceString
import com.example.explory.ui.theme.S12_W600
import com.example.explory.ui.theme.S14_W600
import com.example.explory.ui.theme.S16_W600
import com.example.explory.ui.theme.S18_W600
import com.example.explory.ui.theme.Yellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderItem(user: LocationStatisticDto, currentScreen: Int, userPosition: Int) {
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
        ) {
            if (userPosition == 1) {
                Icon(
                    painter = painterResource(id = R.drawable.crown),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = Yellow
                )
            } else {
                Text(
                    text = userPosition.toString(),
                    style = S18_W600,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }


            Spacer(modifier = Modifier.width(20.dp))
            Avatar(
                image = user.profileDto.avatarUrl,
                border = user.profileDto.inventoryDto.avatarFrames,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier
            ) {
                Text(
                    text = user.profileDto.username,
                    style = S14_W600,
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = user.profileDto.email.toString(),
                    style = S12_W600,
                    color = MaterialTheme.colorScheme.onSurface,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        when (currentScreen) {
            1 -> {
                Row {
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                        tooltip = {
                            Text(
                                text = user.distance?.toInt().toString(),
                                style = S16_W600,
                                color = MaterialTheme.colorScheme.primary
                            )
                        },
                        state = rememberTooltipState()
                    ) {
                        Text(
                            text = getDistanceString(user.distance?.toInt() ?: 0),
                            style = S16_W600,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

            }

            2 -> {
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                    tooltip = {
                        Text(
                            text = user.experience.toString(),
                            style = S16_W600,
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    state = rememberTooltipState()
                ) {
                    Row {
                        Text(
                            text = user.level.toString(),
                            style = S16_W600,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Image(
                            painter = painterResource(id = R.drawable.star),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                        )
                    }
                }
            }
        }
    }
}