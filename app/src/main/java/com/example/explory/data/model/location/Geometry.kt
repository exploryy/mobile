package com.example.explory.data.model.location

import kotlinx.serialization.Serializable

@Serializable
data class Geometry(
    val type: String,
    val coordinates: List<List<List<List<Double>>>>
)

