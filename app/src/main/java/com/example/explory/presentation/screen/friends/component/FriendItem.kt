package com.example.explory.presentation.screen.friends.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.explory.R
import com.example.explory.data.model.friend.Friend
import com.example.explory.presentation.screen.friends.DeleteFriendDialog

@Composable
fun FriendItem(
    friend: Friend,
    toggleBestFriend: (String) -> Unit,
    onDeleteFriendButtonClick: () -> Unit,
    onDeleteFriend: (String) -> Unit,
    isRemoveFriendState: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val imageUrl = friend.avatar

        Box(
            modifier = Modifier
                .size(64.dp)
                .background(Color.DarkGray, shape = CircleShape)
        ) {
            if (imageUrl != null) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.Center)
                        .clip(CircleShape)
                )
            } else {
                Icon(
                    painter = painterResource(R.drawable.picture),
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                        .align(Alignment.Center),
                    tint = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(text = friend.name, style = MaterialTheme.typography.bodyMedium)
            Text(text = friend.email, style = MaterialTheme.typography.bodyMedium)
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