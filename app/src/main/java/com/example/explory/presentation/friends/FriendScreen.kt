package com.example.explory.presentation.friends
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.explory.R
import com.example.explory.data.model.Friend
import com.example.explory.presentation.friends.component.FriendItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendsSheet(
    friends: List<Friend>,
    onInviteFriends: () -> Unit,
    onDismissRequest: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    var searchText by remember { mutableStateOf("") }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { onDismissRequest() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Друзья",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Button(
                onClick = onInviteFriends,
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
}



@Composable
fun FriendsScreen(
    onDismissRequest: () -> Unit
) {
    val friends = listOf(
        Friend("fedosssssss", "3 км", R.drawable.picture, isBestFriend = true),
        Friend("vag55", "3 км", R.drawable.picture, isBestFriend = true),
        Friend("сахарок", "3 км", R.drawable.picture, isBestFriend = false),
        Friend("liiid", "давно не видели", R.drawable.picture, isBestFriend = false),
        Friend("fedosssssss", "3 км", R.drawable.picture, isBestFriend = false),
        Friend("vag55", "3 км", R.drawable.picture, isBestFriend = false),
        Friend("сахарок", "3 км", R.drawable.picture, isBestFriend = false),
        Friend("liiid", "давно не видели", R.drawable.picture, isBestFriend = false),
        Friend("fedosssssss", "3 км", R.drawable.picture, isBestFriend = false),
        Friend("vag55", "3 км", R.drawable.picture, isBestFriend = false),
        Friend("сахарок", "3 км", R.drawable.picture, isBestFriend = false),
        Friend("liiid", "давно не видели", R.drawable.picture, isBestFriend = false),
        Friend("fedosssssss", "3 км", R.drawable.picture, isBestFriend = false),
        Friend("vag55", "3 км", R.drawable.picture, isBestFriend = false),
        Friend("сахарок", "3 км", R.drawable.picture, isBestFriend = false),
        Friend("liiid", "давно не видели", R.drawable.picture, isBestFriend = false),
    )

    FriendsSheet(
        friends = friends,
        onInviteFriends = {  },
        onDismissRequest = { onDismissRequest() }
    )
}
