package com.example.explory.presentation.screen.friends

import com.example.explory.data.model.profile.ProfileDto

data class UserListState(
    val users: List<ProfileDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val addedFriends: List<String> = emptyList(),
    val sentFriendRequests: List<ProfileDto> = emptyList()
)