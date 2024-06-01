package com.example.explory.presentation.screen.profile

import com.example.explory.data.model.Profile

data class ProfileState(
    val profile: Profile? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)