package com.example.explory.presentation.screen.requests

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explory.data.model.requests.FriendRequest
import com.example.explory.domain.usecase.AcceptFriendUseCase
import com.example.explory.domain.usecase.DeclineFriendUseCase
import com.example.explory.domain.usecase.GetFriendRequestsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FriendRequestsViewModel(
    private val getFriendRequestsUseCase: GetFriendRequestsUseCase,
    private val acceptFriendUseCase: AcceptFriendUseCase,
    private val declineFriendUseCase: DeclineFriendUseCase
) : ViewModel() {
    private val _friendRequests = MutableStateFlow(FriendRequest(emptyList(), emptyList()))
    val friendRequests: StateFlow<FriendRequest> = _friendRequests.asStateFlow()

    init {
        fetchFriendRequests()
    }

    fun fetchFriendRequests() {
        viewModelScope.launch {
            try {
                val response = getFriendRequestsUseCase.execute()
                _friendRequests.value = response
            } catch (e: Exception) {
                Log.e("FriendRequestsViewModel", "Error fetching friend requests", e)
            }
        }
    }

    fun acceptRequest(requestId: String) {
        viewModelScope.launch {
            try {
                acceptFriendUseCase.execute(requestId)
                fetchFriendRequests()
            } catch (e: Exception) {
                Log.e("FriendRequestsViewModel", "Error accepting friend request", e)
            }
        }
    }

    fun rejectRequest(requestId: String) {
        viewModelScope.launch {
            try {
                declineFriendUseCase.execute(requestId)
                fetchFriendRequests()
            } catch (e: Exception) {
                Log.e("FriendRequestsViewModel", "Error rejecting friend request", e)
            }
        }
    }
}