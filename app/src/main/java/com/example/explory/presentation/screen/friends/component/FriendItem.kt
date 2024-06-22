package com.example.explory.presentation.screen.friends.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.explory.R
import com.example.explory.data.model.friend.Friend
import com.example.explory.presentation.screen.friends.DeleteFriendDialog
import com.example.explory.presentation.screen.map.component.Avatar
import com.example.explory.ui.theme.S14_W400
import com.example.explory.ui.theme.S16_W600
import com.example.explory.ui.theme.White
import com.example.explory.ui.theme.Yellow

@Composable
fun FriendItem(
    friend: Friend,
    toggleBestFriend: (String) -> Unit,
    onDeleteFriendButtonClick: () -> Unit,
    onDeleteFriend: (String) -> Unit,
    isRemoveFriendState: Boolean,
    onFriendProfileClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onFriendProfileClick(friend.userId) }
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Color.DarkGray, shape = CircleShape)
            ) {
                Avatar(
                    image = friend.avatar,
                    border = friend.inventoryDto.avatarFrames,
                    modifier = Modifier
                        .size(64.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier
            ) {
                Text(
                    text = friend.name,
                    style = S16_W600,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = friend.email,
                    style = S14_W400,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }


        Spacer(modifier = Modifier.weight(1f))


        IconButton(
            onClick = {
                toggleBestFriend(friend.userId)
            },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                painter = if (friend.isBestFriend) painterResource(id = R.drawable.star) else painterResource(
                    id = R.drawable.star_empty
                ),
                contentDescription = "Favorite icon",
                tint = if (!friend.isBestFriend) White else Yellow,
                modifier = Modifier.size(20.dp)
            )
        }

        IconButton(
            onClick = {
                onDeleteFriendButtonClick()
            }, modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Remove icon",
                tint = MaterialTheme.colorScheme.onError,
                modifier = Modifier.size(24.dp)
            )
        }
    }

    if (isRemoveFriendState) {
        DeleteFriendDialog(
            friend = friend,
            deleteFriend = { onDeleteFriend(it) },
            onDismiss = { onDeleteFriendButtonClick() }
        )
    }
}