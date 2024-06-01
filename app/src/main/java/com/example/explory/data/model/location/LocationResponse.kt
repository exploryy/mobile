package com.example.explory.data.model.location

import kotlinx.serialization.Serializable

@Serializable
data class LocationResponse(
    val area_percent: Double,
    val geo: String
)