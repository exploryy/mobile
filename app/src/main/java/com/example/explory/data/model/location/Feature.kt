package com.example.explory.data.model.location

import kotlinx.serialization.Serializable

@Serializable
data class Feature(
    val type: String,
    val geometry: Geometry,
    val properties: Map<String, String>
)