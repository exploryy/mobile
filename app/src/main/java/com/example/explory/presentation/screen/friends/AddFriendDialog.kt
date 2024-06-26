package com.example.explory.presentation.screen.friends

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.explory.presentation.screen.friends.component.UserItem
import com.example.explory.ui.theme.S16_W600
import com.example.explory.ui.theme.S18_W600
import com.example.explory.ui.theme.S20_W600

@Composable
fun AddFriendDialog(
    userListState: UserListState,
    addFriendStatus: AddFriendStatus,
    searchUsers: (String) -> Unit,
    addFriend: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val searchText = remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
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
                    text = "найти друзей",
                    style = S20_W600
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = searchText.value,
                    onValueChange = { newText ->
                        searchText.value = newText
                        searchUsers(newText)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground
                    ),
                    singleLine = true,
                    label = { Text("поиск", style = S16_W600) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (userListState.users.isEmpty() && !userListState.isLoading) {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "введите имя пользователя в поле поиска",
                        style = S18_W600,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                }

                when {
                    userListState.isLoading -> CircularProgressIndicator()

                    userListState.error != null -> Text(
                        "Ошибка: ${userListState.error}",
                        style = S18_W600,
                        color = MaterialTheme.colorScheme.onError
                    )

                    else -> LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        Log.d("Test", userListState.sentFriendRequests.toString())
                        items(userListState.users) { profile ->
                            UserItem(
                                profile = profile,
                                onAddFriend = { userId ->
                                    addFriend(userId)
                                },
                                isAdded = userListState.addedFriends.contains(profile.userId),
                                isRequested = userListState.sentFriendRequests.contains(profile)
                            )
                        }
                    }
                }

                when (addFriendStatus) {
                    is AddFriendStatus.Loading -> {
                        CircularProgressIndicator()
                    }

                    else -> {}
                }

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.onBackground
                        )
                    ) {
                        Text("закрыть", style = S16_W600)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewAddFriendDialog() {
    AddFriendDialog(
        userListState = UserListState(
            users = emptyList(),
            isLoading = false,
            error = null,
            addedFriends = emptyList(),
            sentFriendRequests = emptyList()
        ),
        addFriendStatus = AddFriendStatus.Idle,
        searchUsers = {},
        addFriend = {},
        onDismiss = {}
    )
}