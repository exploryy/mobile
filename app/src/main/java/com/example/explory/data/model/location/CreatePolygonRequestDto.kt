package com.example.explory.data.model.location

import kotlinx.serialization.Serializable

@Serializable
data class CreatePolygonRequestDto(
    val createPolygonRequestDto: FriendLocation,
    val userId: String
)