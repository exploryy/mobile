package com.example.explory.presentation.screen.map

import com.example.explory.data.service.CoinDto
import com.example.explory.data.service.DistanceQuestDto
import com.example.explory.data.service.PointToPointQuestDto
import com.example.explory.data.service.QuestDto
import com.example.explory.presentation.utils.UiState
import com.mapbox.geojson.LineString

data class MapState(
    val uiState: UiState = UiState.Default,
    val polygons: List<List<List<List<Double>>>>? = null,
    val userPosition: Pair<Double, Double>? = null,
    val innerPoints: List<LineString> = emptyList(),
    val quests: List<QuestDto> = emptyList(),
    val coins: List<CoinDto> = emptyList(),
    val showMap: Boolean = false,
    val showRequestPermissionButton: Boolean = false,
    val permissionRequestCount: Int = 1,
    val showFriendsScreen: Boolean = false,
    val showSettingsScreen: Boolean = false,
    val currentLocationPercent: Double = 0.0,
    val currentLocationName: String = "",
    val showViewAnnotationIndex: Int? = null,
    val p2pQuest: PointToPointQuestDto? = null,
    val distanceQuest: DistanceQuestDto? = null,
)