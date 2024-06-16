package com.example.explory.data.model.location

import kotlinx.serialization.Serializable

@Serializable
data class LocationRequest(
    val longitude: String,
    val latitude: String,
    val figureType: String,
    val place: String
)