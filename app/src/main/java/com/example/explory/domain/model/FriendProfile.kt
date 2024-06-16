package com.example.explory.domain.model

import com.mapbox.geojson.Point

data class FriendProfile(
    val id: String,
    val polygons: List<List<Point>>
)