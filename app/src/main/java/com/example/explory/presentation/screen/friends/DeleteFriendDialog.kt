package com.example.explory.presentation.screen.friends

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.explory.data.model.friend.Friend
import com.example.explory.presentation.screen.map.component.Avatar

@Composable
fun DeleteFriendDialog(
    friend: Friend,
    deleteFriend: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Удалить друга?",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(32.dp))

                val imageUrl = friend.avatar

                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color.DarkGray, shape = CircleShape)
                ) {
                    Avatar(
                        image = imageUrl,
                        border = friend.inventoryDto.avatarFrames,
                        modifier = Modifier
                            .size(100.dp)
                            .align(Alignment.Center)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = friend.name,
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    TextButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color.White
                        )
                    ) {
                        Text("Закрыть")
                    }

                    TextButton(
                        onClick = { deleteFriend(friend.userId) },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color.Red
                        )
                    ) {
                        Text("Удалить")
                    }
                }
            }
        }
    }
}