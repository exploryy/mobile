package com.example.explory.presentation.screen.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.example.explory.R
import com.example.explory.data.model.location.FriendLocationDto
import com.example.explory.data.model.location.LocationRequest
import com.example.explory.data.repository.QuestRepository
import com.example.explory.data.service.DistanceQuestDto
import com.example.explory.data.service.PointToPointQuestDto
import com.example.explory.domain.usecase.GetCoinsUseCase
import com.example.explory.domain.usecase.GetFriendStatisticUseCase
import com.example.explory.domain.usecase.GetPolygonsUseCase
import com.example.explory.domain.usecase.GetQuestsUseCase
import com.example.explory.domain.websocket.FriendsLocationWebSocketClient
import com.example.explory.domain.websocket.LocationTracker
import com.example.explory.domain.websocket.LocationWebSocketClient
import com.example.explory.ui.theme.AccentColor
import com.example.explory.ui.theme.Green
import com.example.explory.ui.theme.Red
import com.example.explory.ui.theme.Yellow
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin

class MapViewModel(
    private val getPolygonsUseCase: GetPolygonsUseCase,
    private val getQuestsUseCase: GetQuestsUseCase,
    private val getCoinsUseCase: GetCoinsUseCase,
    private val questRepository: QuestRepository,
    private val webSocketClient: LocationWebSocketClient,
    private val getFriendStatisticUseCase: GetFriendStatisticUseCase,
    private val friendsLocationWebSocketClient: FriendsLocationWebSocketClient,
    private val locationTracker: LocationTracker,
    private val context: Context
) : ViewModel() {
    private val _mapState = MutableStateFlow(MapState())
    val mapState = _mapState.asStateFlow()

    private var lastSentLocation: LocationRequest? = null
    private var lastSentTime: Long = 0
    private val minDistanceChange = 7
    private val minTimeInterval = 30000L

    val outerLineString: LineString = LineString.fromLngLats(
        listOf(
            Point.fromLngLat(180.0, 90.0),
            Point.fromLngLat(180.0, -90.0),
            Point.fromLngLat(-180.0, -90.0),
            Point.fromLngLat(-180.0, 90.0),
            Point.fromLngLat(180.0, 90.0)
        )
    )

    init {
        getStartData()
//        getStartPolygons()
        webSocketClient.connect()
        friendsLocationWebSocketClient.connect()
        startLocationUpdates()
        observeWebSocketMessages()
        observeFriendsLocationWebSocketMessages()
        loadFriendStatistics()
    }

    fun getQuestDetails(questId: String, questType: String) {
        viewModelScope.launch {
            when (questType) {
                "DISTANCE" -> {
                    val distanceQuest = questRepository.getDistanceQuest(questId)
                    updateDistanceQuest(distanceQuest)
                    updateP2PQuest(null)
                }

                "POINT_TO_POINT" -> {
                    val p2pQuest = questRepository.getP2PQuest(questId)
                    updateP2PQuest(p2pQuest)
                    updateDistanceQuest(null)
                }
            }
        }
    }

    private fun getStartPolygons() {
        viewModelScope.launch {
            val polygons = getPolygonsUseCase.execute()
            val newInnerPoints = polygons.features.flatMap { feature ->
                feature.geometry.coordinates.flatMap { coordinateList ->
                    coordinateList.map { innerList ->
                        LineString.fromLngLats(innerList.map { coordinates ->
                            Point.fromLngLat(coordinates[0], coordinates[1])
                        })
                    }
                }
            }
            onInnerListUpdate(newInnerPoints)
        }
    }

    private fun startLocationUpdates() {
        viewModelScope.launch {
            locationTracker.setLocationListener { location ->
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses: List<Address>? =
                    geocoder.getFromLocation(location.latitude, location.longitude, 1)
                if (!addresses.isNullOrEmpty())
                    if (addresses[0].locality != _mapState.value.currentLocationName) {
                        if (addresses[0].locality != null)
                            onCurrentLocationCityChanged(addresses[0].locality)
                        else if (addresses[0].subAdminArea != null)
                            onCurrentLocationCityChanged(addresses[0].subAdminArea)
                        else if (addresses[0].adminArea != null)
                            onCurrentLocationCityChanged(addresses[0].adminArea)
                        else if (addresses[0].countryName != null)
                            onCurrentLocationCityChanged(addresses[0].countryName)
                    }

                val currentTime = System.currentTimeMillis()
                val locationRequest = LocationRequest(
                    longitude = location.longitude.toString(),
                    latitude = location.latitude.toString(),
                    figureType = "CIRCLE"
                )

                if (shouldSendLocation(locationRequest, currentTime)) {
                    sendLocationToServer(locationRequest)
                    lastSentLocation = locationRequest
                    lastSentTime = currentTime
                }
            }
            locationTracker.startTracking()
        }
    }

    private fun sendLocationToServer(locationRequest: LocationRequest) {
        webSocketClient.sendLocationRequest(locationRequest)
    }

    private fun shouldSendLocation(locationRequest: LocationRequest, currentTime: Long): Boolean {
        if (lastSentLocation == null) {
            return true
        }

        val distance = calculateDistance(lastSentLocation!!, locationRequest)
        val timeElapsed = currentTime - lastSentTime

        return distance >= minDistanceChange || timeElapsed >= minTimeInterval
    }

    private fun calculateDistance(loc1: LocationRequest, loc2: LocationRequest): Float {
        val results = FloatArray(1)
        android.location.Location.distanceBetween(
            loc1.latitude.toDouble(),
            loc1.longitude.toDouble(),
            loc2.latitude.toDouble(),
            loc2.longitude.toDouble(),
            results
        )
        return results[0]
    }

    private fun getStartData() {
        viewModelScope.launch {
            getQuests()
            getCoins()
        }
    }

    private suspend fun getQuests() {
        try {
            val quests = getQuestsUseCase.execute()
            _mapState.update {
                it.copy(quests = quests)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun getCoins() {
        try {
            val coins = getCoinsUseCase.execute()
            _mapState.update {
                it.copy(coins = coins)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getPointsForCircle(
        latitude: Double,
        longitude: Double,
        radiusInMeters: Double
    ): List<List<Point>> {
        val earthRadius = 6378137.0
        val numberOfSides = 100
        val deltaLat =
            Math.toDegrees(radiusInMeters / earthRadius)
        val deltaLon = deltaLat / cos(Math.toRadians(latitude))


        val coordinates = mutableListOf<Point>()
        for (i in 0 until numberOfSides) {
            val angle = Math.toRadians(i * 360.0 / numberOfSides)
            val x: Double = longitude + deltaLon * cos(angle)
            val y: Double = latitude + deltaLat * sin(angle)
            coordinates.add(Point.fromLngLat(x, y))
        }
        coordinates.add(coordinates[0])

        return listOf(coordinates)
    }

//    fun getPointsForSquare(
//        latitude: Double,
//        longitude: Double,
//        sideInMeters: Double
//    ): List<Point> {
//    }

    fun getCorrectTransportType(transportType: String): String {
        return when (transportType) {
            "WALK" -> "пешком"
            "CAR" -> "на машине"
            "BICYCLE" -> "на велосипеде"
            "MOTORCYCLE" -> "на мотоцикле"
            else -> "неизвестно"
        }
    }

    fun getCorrectDifficulty(difficulty: String): String {
        return when (difficulty) {
            "EASY" -> "легкий"
            "MEDIUM" -> "средний"
            "HARD" -> "сложный"
            else -> "неизвестно"
        }
    }

    fun getColorByDifficulty(difficulty: String): Color {
        return when (difficulty) {
            "EASY" -> Green
            "MEDIUM" -> Yellow
            "HARD" -> Red
            else -> AccentColor
        }
    }

    fun getNameByType(type: String): String {
        return when (type) {
            "POINT_TO_POINT" -> "Добраться до точки"
            "FIND" -> "Найти"
            "PHOTO" -> "Сделать фото"
            "ANSWER" -> "Ответить на вопрос"
            "DISTANCE" -> "Пройти расстояние"
            else -> "Неизвестно"
        }
    }

    private fun onInnerListUpdate(newInnerPoints: List<LineString>) {
        _mapState.update { it.copy(innerPoints = newInnerPoints) }
    }

    private fun observeWebSocketMessages() {
        viewModelScope.launch {
            webSocketClient.messages.collect { response ->
                response?.let {
                    val newInnerPoints = it.geo.features.flatMap { feature ->
                        feature.geometry.coordinates.flatMap { coordinateList ->
                            coordinateList.map { innerList ->
                                LineString.fromLngLats(innerList.map { coordinates ->
                                    Point.fromLngLat(coordinates[0], coordinates[1])
                                })
                            }
                        }
                    }
                    onInnerListUpdate(newInnerPoints)
                    onAreaUpdate(it.areaPercent)
                }
            }
        }
    }

    private fun loadFriendStatistics() {
        viewModelScope.launch {
            try {
                val friendStats = getFriendStatisticUseCase.execute()
                val friendLocations = friendStats.associate {
                    it.userId to (it.previousLatitude.toDouble() to it.previousLongitude.toDouble())
                }
                val friendAvatars = friendStats.associate { friendStat ->
                    friendStat.userId to Pair(friendStat.name, loadAvatar(friendStat.photoUrl))
                }
                _mapState.update { state ->
                    state.copy(
                        friendsLocations = friendLocations,
                        friendAvatars = friendAvatars
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun loadAvatar(avatarUrl: String?): Bitmap? {
        return try {
            if (avatarUrl.isNullOrEmpty()) {
                null
            } else {
                withContext(Dispatchers.IO) {
                    Glide.with(context)
                        .asBitmap()
                        .load(avatarUrl)
                        .submit()
                        .get()
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            Log.d("FileNotFoundException", "File not found: $avatarUrl")
            val defaultImageResource = R.drawable.picture
            BitmapFactory.decodeResource(context.resources, defaultImageResource)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("Exception", "An unexpected error occurred")
            val defaultImageResource = R.drawable.picture
            BitmapFactory.decodeResource(context.resources, defaultImageResource)
        }
    }

    private fun observeFriendsLocationWebSocketMessages() {
        viewModelScope.launch {
            friendsLocationWebSocketClient.messages.collect { friendLocation ->
                friendLocation?.let {
                    onFriendsLocationUpdate(it)
                }
            }
        }
    }

    private fun onFriendsLocationUpdate(friendLocation: FriendLocationDto) {
        _mapState.update { state ->
            val updatedLocations = state.friendsLocations.toMutableMap()
            updatedLocations[friendLocation.clientId] =
                friendLocation.latitude to friendLocation.longitude
            state.copy(friendsLocations = updatedLocations)
        }
    }



    override fun onCleared() {
        super.onCleared()
        webSocketClient.close()
        friendsLocationWebSocketClient.close()
    }

    private fun onAreaUpdate(areaPercent: Double) {
        _mapState.update { it.copy(currentLocationPercent = areaPercent) }
    }

    fun updateShowViewAnnotationIndex(index: Int?) {
        _mapState.update { it.copy(showViewAnnotationIndex = index) }
    }

    fun updateShowMap(show: Boolean) {
        _mapState.update { it.copy(showMap = show) }
    }

    fun updateShowRequestPermissionButton(show: Boolean) {
        _mapState.update { it.copy(showRequestPermissionButton = show) }
    }

    fun incrementPermissionRequestCount() {
        _mapState.update { it.copy(permissionRequestCount = it.permissionRequestCount + 1) }
    }

    fun updateShowFriendScreen() {
        _mapState.update { it.copy(showFriendsScreen = !it.showFriendsScreen) }
    }

    private fun onCurrentLocationCityChanged(locality: String?) {
        _mapState.update { it.copy(currentLocationName = locality ?: "") }
    }

    fun updateP2PQuest(p2pQuest: PointToPointQuestDto?) {
        _mapState.update { it.copy(p2pQuest = p2pQuest) }
    }

    fun updateDistanceQuest(distanceQuest: DistanceQuestDto?) {
        _mapState.update { it.copy(distanceQuest = distanceQuest) }
    }

    fun updateShowSettingsScreen(){
        _mapState.update { it.copy(showSettingsScreen = !it.showSettingsScreen) }
    }
}



