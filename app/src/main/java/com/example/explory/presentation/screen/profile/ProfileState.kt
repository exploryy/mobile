package com.example.explory.presentation.screen.profile

import com.example.explory.data.model.profile.Profile

data class ProfileState(
    val profile: Profile? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isEditDialogOpen: Boolean = false
)