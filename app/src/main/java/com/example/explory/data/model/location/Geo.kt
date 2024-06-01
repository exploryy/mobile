package com.example.explory.data.model.location

import kotlinx.serialization.Serializable

@Serializable
data class Geo(
    val type: String,
    val features: List<Feature>
)