package com.example.explory.data.model.location

import kotlinx.serialization.Serializable

@Serializable
data class LocationResponse(
    val geo: Geo,
    val areaPercent: Double
)