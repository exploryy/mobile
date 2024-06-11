package com.example.explory.data.model.location

data class FeatureResponse(
    val type: String, val properties: Map<String, String>, val geometry: GeometryResponse
)