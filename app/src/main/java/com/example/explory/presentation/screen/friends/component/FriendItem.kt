package com.example.explory.presentation.screen.friends.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.explory.data.model.friend.Friend
import com.example.explory.presentation.screen.friends.DeleteFriendDialog
import com.example.explory.presentation.screen.map.component.Avatar

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
        ){
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

            Column (
                modifier = Modifier
            ){
                Text(text = friend.name, style = MaterialTheme.typography.bodyMedium)
                Text(text = friend.email, style = MaterialTheme.typography.bodyMedium)
            }
        }


        Spacer(modifier = Modifier.weight(1f))


        IconButton(onClick = {
            toggleBestFriend(friend.userId)
        }) {
            Icon(
                imageVector = if (friend.isBestFriend) Icons.Default.Star else Icons.Default.StarBorder,
                contentDescription = "Notification icon",
                tint = if (friend.isBestFriend) Color.Yellow else Color.White
            )
        }

        IconButton(onClick = {
            onDeleteFriendButtonClick()
        }) {
            Icon(
                imageVector = Icons.Default.DeleteForever,
                contentDescription = "Delete icon",
                tint = Color.Red
            )
        }
    }

    if (isRemoveFriendState){
        DeleteFriendDialog(
            friend = friend,
            deleteFriend = { onDeleteFriend(it) },
            onDismiss = { onDeleteFriendButtonClick() }
        )
    }
}