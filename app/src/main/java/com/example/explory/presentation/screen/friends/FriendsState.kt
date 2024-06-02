package com.example.explory.presentation.screen.friends

import com.example.explory.data.model.friend.Friend


data class FriendsState(
    val friends: List<Friend> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)