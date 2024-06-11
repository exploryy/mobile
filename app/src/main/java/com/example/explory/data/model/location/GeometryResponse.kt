package com.example.explory.data.model.location

data class GeometryResponse(
    val type: String, val coordinates: List<List<List<List<Double>>>>
)