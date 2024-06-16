package com.example.explory.presentation.screen.requests

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.explory.data.model.profile.ProfileDto
import com.example.explory.presentation.screen.map.component.Avatar
import com.example.explory.ui.theme.DarkGreen

@Composable
fun FriendRequestCard(
    request: ProfileDto,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column {
                Avatar(
                    image = request.avatarUrl,
                    border = request.inventoryDto.avatarFrames,
                    modifier = Modifier
                        .size(64.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text (
                    text = request.username,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Text (
                    text = request.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
            }

            Row {
                IconButton(
                    onClick = onAccept,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(DarkGreen)
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "Accept",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                IconButton(
                    onClick = onReject,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.Red)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Reject",
                        tint = Color.White
                    )
                }
            }
        }
    }
}