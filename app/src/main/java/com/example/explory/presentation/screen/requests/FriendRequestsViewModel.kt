package com.example.explory.presentation.screen.requests

import androidx.lifecycle.ViewModel
import com.example.explory.data.model.requests.FriendRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FriendRequestsViewModel : ViewModel() {
    private val _friendRequests = MutableStateFlow<FriendRequest>(FriendRequest(emptyList(), emptyList()))
    val friendRequests: StateFlow<FriendRequest> = _friendRequests.asStateFlow()

    init {
        fetchFriendRequests()
    }

    private fun fetchFriendRequests() {

    }

    fun acceptRequest(requestId: String) {

    }

    fun rejectRequest(requestId: String) {

    }
}
