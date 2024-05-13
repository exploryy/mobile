package com.example.explory.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GeoJson(
    val type: String,
    val coordinates: List<List<List<Double>>>
)