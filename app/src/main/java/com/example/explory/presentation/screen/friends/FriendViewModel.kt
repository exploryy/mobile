package com.example.explory.presentation.screen.friends

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explory.common.FriendMapper
import com.example.explory.domain.usecase.AddFriendUseCase
import com.example.explory.domain.usecase.GetFriendRequestsUseCase
import com.example.explory.domain.usecase.GetFriendsUseCase
import com.example.explory.domain.usecase.GetUserListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FriendViewModel(
    private val getFriendsUseCase: GetFriendsUseCase,
    private val getUserListUseCase: GetUserListUseCase,
    private val addFriendUseCase: AddFriendUseCase,
    private val getFriendRequestsUseCase: GetFriendRequestsUseCase
) : ViewModel() {
    private val _friendsState = MutableStateFlow(FriendsState())
    val friendsState: StateFlow<FriendsState> = _friendsState.asStateFlow()

    private val _userListState = MutableStateFlow(UserListState())
    val userListState: StateFlow<UserListState> = _userListState.asStateFlow()

    private val _addFriendStatus = MutableStateFlow<AddFriendStatus>(AddFriendStatus.Idle)
    val addFriendStatus: StateFlow<AddFriendStatus> = _addFriendStatus.asStateFlow()

    init {
        fetchFriends()
        fetchFriendRequests()
    }

    fun fetchFriends() {
        viewModelScope.launch {
            _friendsState.value = _friendsState.value.copy(isLoading = true)
            try {
                val friendsResponse = getFriendsUseCase.execute()
                val friends = FriendMapper.toFriends(friendsResponse)
                _friendsState.value = _friendsState.value.copy(friends = friends)
            } catch (e: Exception) {
                _friendsState.value = _friendsState.value.copy(error = e.message)
            } finally {
                _friendsState.value = _friendsState.value.copy(isLoading = false)
            }
        }
    }

    fun searchUsers(query: String) {
        viewModelScope.launch {
            _userListState.value = _userListState.value.copy(isLoading = true)
            try {
                val userListResponse = getUserListUseCase.execute(query)
                _userListState.value = _userListState.value.copy(users = userListResponse)
            } catch (e: Exception) {
                _userListState.value = _userListState.value.copy(error = e.message)
            } finally {
                _userListState.value = _userListState.value.copy(isLoading = false)
            }
        }
    }

    private fun fetchFriendRequests() {
        viewModelScope.launch {
            try {
                val requestsResponse = getFriendRequestsUseCase.execute()
                _userListState.value = _userListState.value.copy(
                    sentFriendRequests = requestsResponse.my
                )
                Log.d("Мои заявки", userListState.value.sentFriendRequests.toString())
            } catch (e: Exception) {
                // Что-то
            }
        }
    }

    fun addFriend(userId: String) {
        viewModelScope.launch {
            _addFriendStatus.value = AddFriendStatus.Loading
            try {
                addFriendUseCase.execute(userId)
                _addFriendStatus.value = AddFriendStatus.Success
                _userListState.value = _userListState.value.copy(
                    addedFriends = _userListState.value.addedFriends + userId
                )
                fetchFriends()
            } catch (e: Exception) {
                _addFriendStatus.value = AddFriendStatus.Error(e.message ?: "Error adding friend")
            } finally {
                _addFriendStatus.value = AddFriendStatus.Idle
            }
        }
    }

    fun changeFriendRequestDialogState() {
        _friendsState.update { it.copy(isFriendRequestDialogOpen = !it.isFriendRequestDialogOpen ) }
        _userListState.update { it.copy(users = emptyList()) }
    }
}

