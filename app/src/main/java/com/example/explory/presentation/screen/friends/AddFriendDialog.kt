package com.example.explory.presentation.screen.friends

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.explory.data.model.profile.ProfileDto
import com.example.explory.presentation.screen.friends.component.UserItem
import com.example.explory.ui.theme.DarkGreen
import kotlinx.coroutines.launch

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
                    text = "Найти друзей",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = searchText.value,
                    onValueChange = { newText ->
                        searchText.value = newText
                        searchUsers(newText)
                    },
                    singleLine = true,
                    label = { Text("Поиск пользователей") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (userListState.users.isEmpty() && !userListState.isLoading){
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Найдите своих друзей",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        textAlign = TextAlign.Start
                    )
                }

                when {
                    userListState.isLoading -> CircularProgressIndicator()
                    userListState.error != null -> Text("Ошибка: ${userListState.error}")
                    else -> LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 200.dp)
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

                Spacer(modifier = Modifier.height(16.dp))

                when (addFriendStatus) {
                    is AddFriendStatus.Loading -> {
                        CircularProgressIndicator()
                    }
                    else -> {}
                }

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
                }
            }
        }
    }
}