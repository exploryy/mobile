package com.example.explory.presentation.screen.friends

sealed class AddFriendStatus {
    data object Idle : AddFriendStatus()
    data object Loading : AddFriendStatus()
    data object Success : AddFriendStatus()
    data class Error(val message: String) : AddFriendStatus()
}