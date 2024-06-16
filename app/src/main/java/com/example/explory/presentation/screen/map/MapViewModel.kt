package com.example.explory.presentation.screen.map

import android.annotation.SuppressLint
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
import com.example.explory.data.model.CoinDto
import com.example.explory.data.model.location.FriendLocationDto
import com.example.explory.data.model.location.LocationRequest
import com.example.explory.data.model.quest.DistanceQuestDto
import com.example.explory.data.model.quest.PointToPointQuestDto
import com.example.explory.data.repository.CoinsRepository
import com.example.explory.data.repository.PolygonRepository
import com.example.explory.data.repository.QuestRepository
import com.example.explory.data.websocket.EventDto
import com.example.explory.data.websocket.EventType
import com.example.explory.data.websocket.EventWebSocketClient
import com.example.explory.data.websocket.FriendsLocationWebSocketClient
import com.example.explory.data.websocket.LocationTracker
import com.example.explory.data.websocket.LocationWebSocketClient
import com.example.explory.domain.model.FriendProfile
import com.example.explory.domain.usecase.AcceptFriendUseCase
import com.example.explory.domain.usecase.DeclineFriendUseCase
import com.example.explory.domain.usecase.GetBalanceUseCase
import com.example.explory.domain.usecase.GetCoinsUseCase
import com.example.explory.domain.usecase.GetFriendStatisticUseCase
import com.example.explory.domain.usecase.GetPolygonsUseCase
import com.example.explory.domain.usecase.GetProfileUseCase
import com.example.explory.domain.usecase.GetQuestsUseCase
import com.example.explory.ui.theme.AccentColor
import com.example.explory.ui.theme.Green
import com.example.explory.ui.theme.Red
import com.example.explory.ui.theme.Yellow
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.FileNotFoundException
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin

@SuppressLint("StaticFieldLeak")
class MapViewModel(
    private val getPolygonsUseCase: GetPolygonsUseCase,
    private val getQuestsUseCase: GetQuestsUseCase,
    private val getCoinsUseCase: GetCoinsUseCase,
    private val questRepository: QuestRepository,
    private val coinsRepository: CoinsRepository,
    private val polygonRepository: PolygonRepository,
    private val webSocketClient: LocationWebSocketClient,
    private val eventWebSocketClient: EventWebSocketClient,
    private val getFriendStatisticUseCase: GetFriendStatisticUseCase,
    private val friendsLocationWebSocketClient: FriendsLocationWebSocketClient,
    private val getBalanceUseCase: GetBalanceUseCase,
    private val acceptFriendUseCase: AcceptFriendUseCase,
    private val declineFriendUseCase: DeclineFriendUseCase,
    private val locationTracker: LocationTracker,
    private val getProfileUseCase: GetProfileUseCase,
    private val context: Context
) : ViewModel() {
    private val _mapState = MutableStateFlow(MapState())
    val mapState = _mapState.asStateFlow()

    private var lastSentLocation: LocationRequest? = null
    private var lastSentTime: Long = 0
    private val minDistanceChange = 5
    private val minTimeInterval = 20000L

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
        loadFriendStatistics()
        fetchBalance()
        fetchProfile()
    }

    fun startWebSockets() {
        webSocketClient.connect()
        eventWebSocketClient.connect()
        friendsLocationWebSocketClient.connect()
        startLocationUpdates()
        observeWebSocketMessages()
        observeFriendsLocationWebSocketMessages()
        observeEventWebSocketMessages()
    }

    fun getFriendInfo(friendId: String) {

    }

    private fun calculateDistance(
        firstLat: Double,
        firstLng: Double,
        secondLat: Double,
        secondLng: Double
    ): Double {
        val results = FloatArray(1)
        android.location.Location.distanceBetween(
            firstLat,
            firstLng,
            secondLat,
            secondLng,
            results
        )
        Log.d("MapViewModel", "Distance to user: ${results[0]}")
        return results[0].toDouble()
    }

    private fun fetchProfile() {
        viewModelScope.launch {
            try {
                val profile = getProfileUseCase.execute()
                _mapState.update { it.copy(currentUserFog = profile.inventoryDto.fog) }
            } catch (e: Exception) {
                _mapState.update { it.copy(currentUserFog = null) }
            }
        }
    }

    fun collectCoin(coin: CoinDto, userLocation: Point) {
        viewModelScope.launch {
            try {
                if (calculateDistance(
                        coin.latitude.toDouble(),
                        coin.longitude.toDouble(),
                        userLocation.latitude(),
                        userLocation.longitude()
                    ) > 100.0
                ) {
                    _mapState.update { it.copy(toastText = "Вы слишком далеко от монеты") }
                    return@launch
                }
                coinsRepository.collectCoin(coin.coinId)
                _mapState.update { it ->
                    it.copy(
                        coins = it.coins.filter { it.coinId != coin.coinId },
                        toastText = "Монета собрана"
                    )
                }
                fetchBalance()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun startQuest(questId: String) {
        viewModelScope.launch {
            try {
                Log.d("MapViewModel", "Quest id $questId")
                questRepository.startQuest(questId, "WALK")
                _mapState.update { it ->
                    it.copy(toastText = "Квест начат",
                        activeQuest = it.notCompletedQuests.find { it.questId.toString() == questId },
                        notCompletedQuests = it.notCompletedQuests.filter { it.questId.toString() != questId }
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
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

    fun cancelQuest(questId: String) {
        viewModelScope.launch {
            try {
                questRepository.cancelQuest(questId)
                _mapState.update {
                    it.copy(
                        toastText = "Квест отменен",
                        activeQuest = null,
                        notCompletedQuests = it.notCompletedQuests + it.activeQuest!!
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
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
                updateUserLocation(location.latitude, location.longitude)
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

//                val currentTime = System.currentTimeMillis()
                val locationRequest = LocationRequest(
                    longitude = location.longitude.toString(),
                    latitude = location.latitude.toString(),
                    figureType = "CIRCLE",
                    place = _mapState.value.currentLocationName
                )

//                if (shouldSendLocation(locationRequest, currentTime)) {
//                    sendLocationToServer(locationRequest)
//                    lastSentLocation = locationRequest
//                    lastSentTime = currentTime
//                }
                sendLocationToServer(locationRequest)
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

        val distance = calculateDistance(
            lastSentLocation!!.latitude.toDouble(),
            lastSentLocation!!.longitude.toDouble(),
            locationRequest.latitude.toDouble(),
            locationRequest.longitude.toDouble()
        )
        val timeElapsed = currentTime - lastSentTime

        return distance >= minDistanceChange || timeElapsed >= minTimeInterval
    }

    private fun getStartData() {
        viewModelScope.launch {
            getQuests()
            getCoins()
        }
    }

    private suspend fun getQuests(addNew: Boolean = false) {
        try {
            val quests = getQuestsUseCase.execute()
            if (quests.active.isNotEmpty()) {
                when (quests.active[0].questType) {
                    "DISTANCE" -> {
                        val distanceQuest =
                            questRepository.getDistanceQuest(quests.active[0].questId.toString())
                        updateDistanceQuest(distanceQuest)
                    }

                    "POINT_TO_POINT" -> {
                        val p2pQuest =
                            questRepository.getP2PQuest(quests.active[0].questId.toString())
                        updateP2PQuest(p2pQuest)
                    }
                }
            }
            if (addNew) {
                _mapState.update {
                    it.copy(
                        notCompletedQuests = it.notCompletedQuests + quests.notCompleted,
                        completedQuests = it.completedQuests + quests.completed,
                        activeQuest = if (quests.active.isNotEmpty()) quests.active[0] else null,
                    )
                }
            } else {
                _mapState.update {
                    it.copy(
                        notCompletedQuests = quests.notCompleted,
                        completedQuests = quests.completed,
                        activeQuest = if (quests.active.isNotEmpty()) quests.active[0] else null,
                    )
                }
            }
//            _mapState.update {
//                it.copy(
//                    notCompletedQuests = quests.notCompleted,
//                    completedQuests = quests.completed,
//                    activeQuest = if (quests.active.isNotEmpty()) quests.active[0] else null,
//                )
//            }
        } catch (e: Exception) {
            Log.e("MapViewModel", "Error getting quests", e)
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
                    it.profileDto.userId to (it.previousLatitude.toDouble() to it.previousLongitude.toDouble())
                }
                val friendAvatars = friendStats.associate { friendStat ->
                    friendStat.profileDto.userId to Pair(
                        friendStat.profileDto.username,
                        loadImage(friendStat.profileDto.avatarUrl)
                    )
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

    private fun observeEventWebSocketMessages() {
        viewModelScope.launch {
            eventWebSocketClient.events.collect { event ->
                event.let {
                    when (it.type) {
                        EventType.COMPLETE_QUEST -> {
                            _mapState.update { state -> state.copy(event = it) }
                            updateP2PQuest(null)
                            updateDistanceQuest(null)
                        }

                        EventType.REQUEST_TO_FRIEND -> {
                            _mapState.update { state -> state.copy(event = it) }
                        }

                        EventType.CHANGE_MONEY -> {
                            _mapState.update { state ->
                                state.copy(
                                    userBalance = state.userBalance?.copy(balance = it.text.toInt())
                                )
                            }
                        }

                        EventType.NEW_QUEST -> {
                            getQuests(addNew = true)
                        }

                        EventType.UPDATE_LEVEL -> _mapState.update { state ->
                            fetchBalance()
                            state.copy(
                                userBalance = state.userBalance?.copy(level = it.text.toInt())
                            )
                        }

                        EventType.UPDATE_EXPERIENCE -> _mapState.update { state ->
                            state.copy(
                                userBalance = state.userBalance?.copy(experience = it.text.toInt())
                            )
                        }
                    }
                }
            }
        }
    }


    fun loadImage(imageUrl: String?): Bitmap? {
        return try {
            if (imageUrl.isNullOrEmpty()) {
                null
            } else {
                Glide.with(context)
                    .asBitmap()
                    .load(imageUrl)
                    .submit()
                    .get()
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            Log.d("FileNotFoundException", "File not found: $imageUrl")
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

    private fun fetchBalance() {
        viewModelScope.launch {
            try {
                val result = getBalanceUseCase.execute()
                _mapState.update { it.copy(userBalance = result) }
            } catch (e: Exception) {
                Log.e("BalanceViewModel", "Failed to fetch balance", e)
            }
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

    fun updateShowSettingsScreen() {
        _mapState.update { it.copy(showSettingsScreen = !it.showSettingsScreen) }
    }

    private fun updateUserLocation(latitude: Double, longitude: Double) {
        _mapState.update { it.copy(userPoint = Point.fromLngLat(longitude, latitude)) }
    }

    fun updateToastText(text: String?) {
        _mapState.update { it.copy(toastText = text) }
    }

    fun updateShopOpen() {
        _mapState.update { it.copy(isShopOpen = !it.isShopOpen) }
        fetchBalance()
    }

    fun onFriendMarkerClicked(friendId: String) {
        viewModelScope.launch {
            try {
                val friendPolygons = polygonRepository.getFriendPolygons(friendId)
                val friendProfile = FriendProfile(
                    id = friendId,
                    polygons = friendPolygons.features.flatMap { feature ->
                        feature.geometry.coordinates.flatMap { coordinateList ->
                            coordinateList.map { innerList ->
                                innerList.map { coordinates ->
                                    Point.fromLngLat(coordinates[0], coordinates[1])
                                }
                            }
                        }
                    }
                )
                _mapState.update {
                    it.copy(
                        selectedFriendProfile = friendProfile
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun closeFriendProfileScreen() {
        _mapState.update {
            it.copy(selectedFriendProfile = null)
        }
    }

    fun updateEvent(event: EventDto?) {
        _mapState.update { it.copy(event = event) }
    }

    fun updateInventoryOpenScreen() {
        fetchProfile()
        _mapState.update { it.copy(isInventoryOpen = !it.isInventoryOpen) }
    }

    fun setError(message: String?) {
        if (message != null) {
            _mapState.update { it.withErrorEnqueued(message) }
            if (_mapState.value.currentError == null) {
                showNextError()
            }
        }
    }

    private fun showNextError() {
        val nextError = _mapState.value.errorQueue.peek()
        if (nextError != null) {
            _mapState.update { it.withCurrentError(nextError) }
            clearErrorAfterDelay()
        }
    }

    private fun clearErrorAfterDelay() {
        viewModelScope.launch {
            delay(4000)
            _mapState.update { it.withNextErrorDequeued().withCurrentError(null) }
            showNextError()
        }
    }

    fun acceptFriendRequest(friendId: String) {
        viewModelScope.launch {
            try {
                acceptFriendUseCase.execute(friendId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun declineFriendRequest(friendId: String) {
        viewModelScope.launch {
            try {
                declineFriendUseCase.execute(friendId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}



