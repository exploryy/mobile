package com.example.explory.presentation.screen.friends

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.explory.R
import com.example.explory.data.model.friend.Friend
import com.example.explory.presentation.screen.friends.component.FriendItem

@Composable
fun FriendsScreen() {
    var searchText by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
    ) {
        Text(
            text = "Друзья",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        Button(
            onClick = {  },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Позвать друзей")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = searchText,
            onValueChange = { newText ->
                searchText = newText
            },
            placeholder = { Text("Поиск") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon")
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        val filteredFriends = friends.filter {
            it.name.contains(searchText, ignoreCase = true)
        }

        LazyColumn {
            item {
                Text(
                    text = "Лучшие друзья",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(filteredFriends.filter { it.isBestFriend }) { friend ->
                FriendItem(friend)
            }

            item {
                Text(
                    text = "Все друзья (${filteredFriends.size})",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(filteredFriends) { friend ->
                FriendItem(friend)
            }
        }
    }
}

val friends = listOf(
    Friend(name = "Alice", status = "Online", avatarRes = R.drawable.picture, isBestFriend = true),
    Friend(name = "Bob", status = "Offline", avatarRes = R.drawable.picture, isBestFriend = false),
    Friend(name = "Charlie", status = "Away", avatarRes = R.drawable.picture, isBestFriend = false),
    Friend(name = "David", status = "Busy", avatarRes = R.drawable.picture, isBestFriend = false),
    Friend(name = "Eve", status = "Online", avatarRes = R.drawable.picture, isBestFriend = true)
)
