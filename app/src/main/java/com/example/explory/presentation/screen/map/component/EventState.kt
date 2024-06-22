package com.example.explory.presentation.screen.map.component

import com.example.explory.data.model.profile.FriendProfileDto
import com.example.explory.domain.model.BuffDto

data class EventState(
    val user: FriendProfileDto? = null,
    val buffs: List<BuffDto> = emptyList(),
)
