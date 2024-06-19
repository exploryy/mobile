package com.example.explory.data.model.event

import kotlinx.serialization.Serializable

@Serializable
data class EventDto(
    val text: String,
    val type: EventType
)