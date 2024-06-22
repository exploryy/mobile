package com.example.explory.presentation.screen.requests

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.style.TextAlign
import com.example.explory.ui.theme.S20_W600
import org.koin.androidx.compose.koinViewModel

@Composable
fun FriendRequestsScreen(viewModel: FriendRequestsViewModel = koinViewModel()) {
    val friendRequests by viewModel.friendRequests.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchFriendRequests()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        if (friendRequests.other.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "у вас пока нет\nзаявок в друзья",
                    style = S20_W600,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
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