package com.example.explory.data.model.location

import kotlinx.serialization.Serializable

@Serializable
data class FriendLocation(
    val longitude: Double,
    val latitude: Double,
    val figureType: String,
    val place: String
)