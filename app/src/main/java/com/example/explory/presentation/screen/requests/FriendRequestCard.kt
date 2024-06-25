package com.example.explory.presentation.screen.requests

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.explory.data.model.profile.ProfileDto
import com.example.explory.presentation.screen.map.component.Avatar
import com.example.explory.ui.theme.Green
import com.example.explory.ui.theme.S16_W600

@Composable
fun FriendRequestCard(
    request: ProfileDto,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Avatar(
                image = request.avatarUrl,
                border = request.inventoryDto.avatarFrames,
                modifier = Modifier
                    .size(40.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(
                    modifier = Modifier.widthIn(max = 200.dp)
                ) {
                    Text(
                        text = request.username,
                        style = S16_W600,
                        color = MaterialTheme.colorScheme.onBackground,
                        overflow = TextOverflow.Ellipsis,
                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Text(
//                        text = request.email.toString(),
//                        style = S14_W600,
//                        color = MaterialTheme.colorScheme.onSurface
//                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = onReject,
                    modifier = Modifier
                        .size(48.dp)
                ) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = "Reject",
                        tint = MaterialTheme.colorScheme.onError
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = onAccept,
                    modifier = Modifier
                        .size(48.dp)
                ) {
                    Icon(
                        Icons.Filled.Done,
                        contentDescription = "Accept",
                        tint = Green
                    )
                }

            }
        }
    }
}