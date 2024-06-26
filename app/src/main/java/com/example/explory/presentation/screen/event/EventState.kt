package com.example.explory.presentation.screen.event

import com.example.explory.data.model.profile.ProfileDto
import com.example.explory.domain.model.BuffDto

data class EventState(
    val user: ProfileDto? = null,
    val buffs: List<BuffDto> = emptyList(),
)
