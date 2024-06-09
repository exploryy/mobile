package com.example.explory.data.model.location

import kotlinx.serialization.Serializable

@Serializable
data class FriendLocationDto(
    val clientId: String,
    val latitude: String,
    val longitude: String
)