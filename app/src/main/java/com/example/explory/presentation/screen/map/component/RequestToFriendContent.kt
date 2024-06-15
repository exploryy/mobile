package com.example.explory.presentation.screen.map.component

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.explory.data.websocket.EventDto
import com.example.explory.data.websocket.EventType
import com.example.explory.ui.theme.Black
import com.example.explory.ui.theme.S16_W600
import com.example.explory.ui.theme.S20_W600
import com.example.explory.ui.theme.S24_W600
import com.example.explory.ui.theme.White
import kotlinx.serialization.json.Json

@Composable
fun RequestToFriendContent(
    event: EventDto,
    onAcceptClick: (String) -> Unit,
    onDeclineClick: (String) -> Unit
) {
    val friendDto =
        Json.decodeFromString<FriendShortDto>(event.text)
    Column(
        Modifier
            .width(DIALOG_WIDTH.dp)
            .background(White, shape = RoundedCornerShape(DIALOG_SHAPE.dp))
            .clip(RoundedCornerShape(DIALOG_SHAPE.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Новая заявка!", style = S24_W600, textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(64.dp))
        Avatar(image = friendDto.avatarUrl, modifier = Modifier.size(75.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = friendDto.username, style = S20_W600
        )
        Spacer(modifier = Modifier.height(64.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { onDeclineClick(friendDto.userId) },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = White, containerColor = Black
                )
            ) {
                Text(text = "Отклонить", style = S16_W600)
            }
            Button(
                onClick = { onAcceptClick(friendDto.userId) },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Black, containerColor = White
                )
            ) {
                Text(text = "Принять", style = S16_W600)
            }
        }
    }
}

@Preview
@Composable
private fun PreviewRequestToFriendDialog() {
    EventDialog(event = EventDto(
        text = "Новая заявка", type = EventType.REQUEST_TO_FRIEND
    ), onDismissRequest = {}, onFriendAccept = {}, onFriendDecline = {})
}

data class FriendShortDto(
    val userId: String, val username: String, val avatarUrl: String
)