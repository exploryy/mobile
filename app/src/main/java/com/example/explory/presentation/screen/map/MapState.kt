package com.example.explory.presentation.screen.map

import android.graphics.Bitmap
import com.example.explory.data.model.event.EventDto
import com.example.explory.data.model.inventory.CosmeticItemInInventoryDto
import com.example.explory.data.model.note.CommonNoteDto
import com.example.explory.data.model.quest.CompletedQuestDto
import com.example.explory.data.model.quest.DistanceQuestDto
import com.example.explory.data.model.quest.PointToPointQuestDto
import com.example.explory.data.model.quest.QuestDto
import com.example.explory.data.model.statistic.BalanceDto
import com.example.explory.data.model.statistic.CoinDto
import com.example.explory.domain.model.FriendProfile
import com.example.explory.domain.model.MapNote
import com.example.explory.presentation.utils.UiState
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import java.util.LinkedList
import java.util.Queue

data class MapState(
    val uiState: UiState = UiState.Loading,
    val isDarkTheme: Boolean = false,
    val innerPoints: List<LineString> = emptyList(),
    val completedQuests: List<CompletedQuestDto> = emptyList(),
    val activeQuest: QuestDto? = null,
    val notCompletedQuests: List<QuestDto> = emptyList(),
    val coins: List<CoinDto> = emptyList(),
    val permissionRequestCount: Int = 1,
    val showFriendsScreen: Boolean = false,
    val showSettingsScreen: Boolean = false,
    val currentLocationPercent: Double = 0.0,
    val currentLocationName: String = "",
    val showViewAnnotationIndex: Int? = null,
    val p2pQuest: PointToPointQuestDto? = null,
    val distanceQuest: DistanceQuestDto? = null,
    val friendsLocations: Map<String, Pair<Double, Double>> = emptyMap(),
    val friendAvatars: Map<String, Pair<Bitmap?, Bitmap?>> = emptyMap(),
    val userPoint: Point? = null,
    val infoText: String? = null,
    val selectedFriendProfile: FriendProfile? = null,
    val userBalance: BalanceDto? = null,
    val errorQueue: Queue<String> = LinkedList(),
    val currentError: String? = null,
    val event: EventDto? = null,
    val isShopOpen: Boolean = false,
    val isInventoryOpen: Boolean = false,
    val currentUserFog: CosmeticItemInInventoryDto? = null,
    val currentTrace: CosmeticItemInInventoryDto? = null,
    val isBattlePassOpen: Boolean = false,
    val createNotePoint: Point? = null,
    val noteList: List<CommonNoteDto> = emptyList(),
    val note: MapNote? = null,
    val isLeaderboardOpen: Boolean = false,
    val isPublicPrivacy: Boolean = false,
    val isCompletedQuestOpen: Boolean = false
)

