package com.example.explory.presentation.screen.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explory.common.FriendMapper
import com.example.explory.domain.usecase.GetFriendsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FriendViewModel(private val getFriendsUseCase: GetFriendsUseCase) : ViewModel() {
    private val _friendsState = MutableStateFlow(FriendsState())
    val friendsState: StateFlow<FriendsState> = _friendsState.asStateFlow()

    init {
        fetchFriends()
    }

    fun fetchFriends() {
        viewModelScope.launch {
            _friendsState.value = FriendsState(isLoading = true)
            try {
                val friendsResponse = getFriendsUseCase.execute()
                val friends = FriendMapper.toFriends(friendsResponse)
                _friendsState.value = FriendsState(friends = friends)
            } catch (e: Exception) {
                _friendsState.value = FriendsState(error = e.message)
            } finally {
                _friendsState.value = FriendsState(isLoading = false)
            }
        }
    }
}