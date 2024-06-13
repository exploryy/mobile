package com.example.explory.presentation.screen.map

import android.graphics.Bitmap
import com.example.explory.data.model.CoinDto
import com.example.explory.data.model.quest.CompletedQuestDto
import com.example.explory.data.model.quest.DistanceQuestDto
import com.example.explory.data.model.quest.PointToPointQuestDto
import com.example.explory.data.model.quest.QuestDto
import com.example.explory.presentation.utils.UiState
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point

data class MapState(
    val uiState: UiState = UiState.Default,
    val polygons: List<List<List<List<Double>>>>? = null,
    val innerPoints: List<LineString> = emptyList(),
    val completedQuests: List<CompletedQuestDto> = emptyList(),
    val activeQuest: QuestDto? = null,
    val notCompletedQuests: List<QuestDto> = emptyList(),
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
    val friendsLocations: Map<String, Pair<Double, Double>> = emptyMap(),
    val friendAvatars: Map<String, Pair<String, Bitmap?>> = emptyMap(),
    val questFinished: Boolean = false,
    val userPoint: Point? = null,
    val showFriendProfileScreen: Boolean = false,
    val selectedFriendId: String? = null,
    val errorText: String? = null
)