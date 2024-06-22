package com.example.explory.presentation.screen.friends

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.explory.presentation.screen.auth.component.LoadingItem
import com.example.explory.presentation.screen.friends.component.FriendItem
import com.example.explory.ui.theme.S16_W600
import com.example.explory.ui.theme.S18_W600
import org.koin.androidx.compose.koinViewModel

@Composable
fun FriendsScreen(
    viewModel: FriendViewModel = koinViewModel(),
    onFriendProfileClick: (String) -> Unit
) {
    val friendsState by viewModel.friendsState.collectAsStateWithLifecycle()
    val userListState by viewModel.userListState.collectAsStateWithLifecycle()
    val addFriendStatus by viewModel.addFriendStatus.collectAsStateWithLifecycle()

    val friends = friendsState.friends

    var searchText by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        viewModel.fetchFriends()
    }

    if (friendsState.isFriendRequestDialogOpen) {
        AddFriendDialog(
            onDismiss = { viewModel.changeFriendRequestDialogState() },
            addFriendStatus = addFriendStatus,
            userListState = userListState,
            searchUsers = { viewModel.searchUsers(it) },
            addFriend = { viewModel.addFriend(it) }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
    ) {
        // todo позвать друзей + qr code?
        Button(
            onClick = { viewModel.changeFriendRequestDialogState() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("найти друзей", style = S16_W600)
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (friendsState.isLoading) {
            LoadingItem()
        } else if (friends.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "у вас пока нет друзей :(",
                    style = S18_W600,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        } else {
            OutlinedTextField(
                value = searchText,
                onValueChange = { newText ->
                    searchText = newText
                },
                placeholder = { Text("поиск", style = S16_W600) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                maxLines = 1,
                textStyle = S16_W600,
                colors = OutlinedTextFieldDefaults.colors().copy(
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                ),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon")
                },
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            val filteredFriends = friends.filter {
                it.name.contains(searchText, ignoreCase = true)
            }

            LazyColumn {
                if (filteredFriends.any { it.isBestFriend }) {
                    item {
                        Text(
                            text = "лучшие друзья",
                            style = S18_W600,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }

                items(filteredFriends.filter { it.isBestFriend }) { friend ->
                    FriendItem(
                        friend = friend,
                        toggleBestFriend = { viewModel.toggleFavoriteFriend(it) },
                        onDeleteFriendButtonClick = { viewModel.changeRemoveFriendState() },
                        onDeleteFriend = { viewModel.removeFriend(it) },
                        isRemoveFriendState = friendsState.isStartDeleteFriend,
                        onFriendProfileClick = { onFriendProfileClick(it) }
                    )
                }

                item {
                    Text(
                        text = "все друзья - ${filteredFriends.size}",
                        style = S18_W600,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                items(filteredFriends) { friend ->
                    FriendItem(
                        friend = friend,
                        toggleBestFriend = { viewModel.toggleFavoriteFriend(it) },
                        onDeleteFriendButtonClick = { viewModel.changeRemoveFriendState() },
                        onDeleteFriend = { viewModel.removeFriend(it) },
                        isRemoveFriendState = friendsState.isStartDeleteFriend,
                        onFriendProfileClick = { onFriendProfileClick(it) }
                    )
                }
            }
        }
    }
}
