package com.example.explory.presentation.screen.event

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.explory.data.model.event.EventDto
import com.example.explory.data.model.event.EventType
import com.example.explory.data.model.profile.ProfileDto
import com.example.explory.presentation.common.Avatar
import com.example.explory.ui.theme.S16_W600
import com.example.explory.ui.theme.S20_W600
import com.example.explory.ui.theme.S24_W600

@Composable
fun RequestToFriendContent(
    user: ProfileDto?,
    onAcceptClick: (String) -> Unit,
    onDeclineClick: (String) -> Unit
) {
    Column(
        Modifier
            .width(DIALOG_WIDTH.dp)
            .background(
                MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(DIALOG_SHAPE.dp)
            )
            .clip(RoundedCornerShape(DIALOG_SHAPE.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "новая заявка",
            style = S24_W600,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(64.dp))
        if (user == null) {
            CircularProgressIndicator()
        } else {
            Avatar(
                image = user.avatarUrl,
                border = user.inventoryDto.avatarFrames,
                modifier = Modifier.size(75.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = user.username,
                style = S20_W600,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Spacer(modifier = Modifier.height(64.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    if (user != null) {
                        onDeclineClick(user.userId)
                    }
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.surface,
                    containerColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Text(text = "отклонить", style = S16_W600)
            }
            Button(
                onClick = {
                    if (user != null) {
                        onAcceptClick(user.userId)
                    }
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = "принять", style = S16_W600)
            }
        }
    }
}

@Preview
@Composable
private fun PreviewRequestToFriendDialog() {
    EventDialog(event = EventDto(
        text = "Новая заявка", type = EventType.REQUEST_TO_FRIEND
    ), onDismissRequest = {})
}