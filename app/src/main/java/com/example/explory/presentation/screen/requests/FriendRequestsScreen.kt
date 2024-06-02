package com.example.explory.presentation.screen.requests

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@Composable
fun FriendRequestsScreen(viewModel: FriendRequestsViewModel = koinViewModel()) {
    val friendRequests by viewModel.friendRequests.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchFriendRequests()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Заявки в друзья",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
        )

        if (friendRequests.other.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Нет заявок в друзья",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(friendRequests.other) { request ->
                    FriendRequestCard(
                        request = request,
                        onAccept = { viewModel.acceptRequest(request.userId) },
                        onReject = { viewModel.rejectRequest(request.userId) }
                    )
                }
            }
        }
    }
}

//31d37216-9caf-4526-9080-f2f55c7a3380