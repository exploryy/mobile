package com.example.explory.presentation.screen.map

import com.example.explory.presentation.utils.UiState
import com.mapbox.geojson.LineString

data class MapState(
    val uiState: UiState = UiState.Default,
    val polygons: List<List<List<Double>>>? = null,
    val userPosition: Pair<Double, Double>? = null,
    val innerPoints: List<LineString> = emptyList()
)