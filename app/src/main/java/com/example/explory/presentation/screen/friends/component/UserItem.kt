package com.example.explory.presentation.screen.friends.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.explory.data.model.profile.ProfileDto
import com.example.explory.presentation.common.Avatar
import com.example.explory.ui.theme.S16_W600

@Composable
fun UserItem(
    profile: ProfileDto,
    onAddFriend: (String) -> Unit,
    isAdded: Boolean,
    isRequested: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {

            Avatar(
                image = profile.avatarUrl,
                border = profile.inventoryDto.avatarFrames,
                modifier = Modifier
                    .size(40.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = profile.username,
                    style = S16_W600,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
//                if (profile.email?.isNotBlank() == true) {
//                    Spacer(modifier = Modifier.height(4.dp))
//                    Text(
//                        text = profile.email,
//                        style = S14_W600,
//                        color = MaterialTheme.colorScheme.onSurface,
//                        maxLines = 1,
//                        overflow = TextOverflow.Ellipsis
//                    )
//                }
            }
        }

        TextButton(
            onClick = { onAddFriend(profile.userId) },
            enabled = !isAdded && !isRequested,
            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onBackground)
        ) {
            Text("добавить", style = S16_W600)
        }
    }
}
