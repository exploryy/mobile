package com.example.explory.presentation.screen.profile

import com.example.explory.data.model.profile.Profile

data class ProfileState(
    val profile: Profile? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isEditDialogOpen: Boolean = false,
    val profileScreenState: Int = 1,
    val notificationCount: Int = 0,
    val loggedOut: Boolean = false,
    val showFriendProfileScreen: Boolean = false,
    val selectedFriendId: String? = null
)