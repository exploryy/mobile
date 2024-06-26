package com.example.explory.presentation.screen.map

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.example.explory.R
import com.example.explory.data.model.event.EventDto
import com.example.explory.data.model.event.EventType
import com.example.explory.data.model.location.CreatePolygonRequestDto
import com.example.explory.data.model.location.LocationRequest
import com.example.explory.data.model.note.NoteMultipart
import com.example.explory.data.model.quest.DifficultyType
import com.example.explory.data.model.quest.DistanceQuestDto
import com.example.explory.data.model.quest.PointToPointQuestDto
import com.example.explory.data.model.quest.TransportType
import com.example.explory.data.model.statistic.CoinDto
import com.example.explory.data.repository.CoinsRepository
import com.example.explory.data.repository.PolygonRepository
import com.example.explory.data.repository.QuestRepository
import com.example.explory.data.repository.StatisticRepository
import com.example.explory.data.storage.ThemePreferenceManager
import com.example.explory.data.websocket.EventWebSocketClient
import com.example.explory.data.websocket.FriendsLocationWebSocketClient
import com.example.explory.data.websocket.LocationTracker
import com.example.explory.data.websocket.LocationWebSocketClient
import com.example.explory.domain.model.FriendProfile
import com.example.explory.domain.model.MapNote
import com.example.explory.domain.usecase.AcceptFriendUseCase
import com.example.explory.domain.usecase.CreateNoteUseCase
import com.example.explory.domain.usecase.DeclineFriendUseCase
import com.example.explory.domain.usecase.GetAllNotesUseCase
import com.example.explory.domain.usecase.GetBalanceUseCase
import com.example.explory.domain.usecase.GetCoinsUseCase
import com.example.explory.domain.usecase.GetFriendStatisticUseCase
import com.example.explory.domain.usecase.GetNoteUseCase
import com.example.explory.domain.usecase.GetProfileUseCase
import com.example.explory.domain.usecase.GetQuestsUseCase
import com.example.explory.presentation.utils.UiState
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

@SuppressLint("StaticFieldLeak")
class MapViewModel(
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
    private val themePreferenceManager: ThemePreferenceManager,
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val getNoteUseCase: GetNoteUseCase,
    private val createNoteUseCase: CreateNoteUseCase,
    private val statisticRepository: StatisticRepository,
    private val context: Context
) : ViewModel() {
    private val _mapState = MutableStateFlow(MapState())
    val mapState = _mapState.asStateFlow()

    val outerLineString: LineString = LineString.fromLngLats(
        listOf(
            Point.fromLngLat(180.0, 90.0),
            Point.fromLngLat(180.0, -90.0),
            Point.fromLngLat(-180.0, -90.0),
            Point.fromLngLat(-180.0, 90.0),
            Point.fromLngLat(180.0, 90.0)
        )
    )

    private fun startWebSockets() {
        startLocationUpdates()
        webSocketClient.connect()
        eventWebSocketClient.connect()
        friendsLocationWebSocketClient.connect()
        observeWebSocketMessages()
        observeFriendsLocationWebSocketMessages()
        observeEventWebSocketMessages()
        observeWebSocketConnection()
    }

    private fun calculateDistance(
        firstLat: Double, firstLng: Double, secondLat: Double, secondLng: Double
    ): Double {
        val results = FloatArray(1)
        android.location.Location.distanceBetween(
            firstLat, firstLng, secondLat, secondLng, results
        )
        return results[0].toDouble()
    }

    private suspend fun fetchProfile() {
        try {
            val profile = getProfileUseCase.execute()
            _mapState.update {
                it.copy(
                    currentUserFog = profile.inventoryDto.fog,
                    currentTrace = profile.inventoryDto.footprint
                )
            }
        } catch (e: Exception) {
            _mapState.update { it.copy(currentUserFog = null) }
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
                    _mapState.update { it.copy(infoText = "Вы слишком далеко от монеты") }
                    return@launch
                }
                coinsRepository.collectCoin(coin.coinId)
                _mapState.update { it ->
                    it.copy(
                        coins = it.coins.filter { it.coinId != coin.coinId },
                        infoText = "Монета собрана"
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun startQuest(questId: String, transportType: TransportType) {
        viewModelScope.launch {
            try {
                Log.d("MapViewModel", "Quest id $questId")
                val quest =
                    _mapState.value.notCompletedQuests.find { it.questId.toString() == questId }
                if (calculateDistance(
                        _mapState.value.userPoint?.latitude() ?: 0.0,
                        _mapState.value.userPoint?.longitude() ?: 0.0,
                        quest?.latitude?.toDouble() ?: 0.0,
                        quest?.longitude?.toDouble() ?: 0.0
                    ) > 100.0
                ) {
                    _mapState.update { it.copy(infoText = "Вы слишком далеко от квеста") }
                    return@launch
                }
                questRepository.startQuest(questId, transportType)
                _mapState.update { it ->
                    it.copy(infoText = "Квест начат",
                        activeQuest = quest,
                        notCompletedQuests = it.notCompletedQuests.filter { it.questId.toString() != questId })
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
                    Log.d("MapViewModel", "Distance quest: $distanceQuest")
                }

                "POINT_TO_POINT" -> {
                    val p2pQuest = questRepository.getP2PQuest(questId)
                    updateP2PQuest(p2pQuest)
                    updateDistanceQuest(null)
                    Log.d("MapViewModel", "Distance quest: $p2pQuest")

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
                        infoText = "Квест отменен",
                        activeQuest = null,
                        notCompletedQuests = it.notCompletedQuests + it.activeQuest!!
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun updateCurrentLocation(latitude: Double, longitude: Double) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            @Suppress("DEPRECATION") val addresses: List<Address>? =
                geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) if (addresses[0].locality != _mapState.value.currentLocationName) {
                if (addresses[0].locality != null) onCurrentLocationCityChanged(addresses[0].locality)
                else if (addresses[0].subAdminArea != null) onCurrentLocationCityChanged(addresses[0].subAdminArea)
                else if (addresses[0].adminArea != null) onCurrentLocationCityChanged(addresses[0].adminArea)
                else if (addresses[0].countryName != null) onCurrentLocationCityChanged(addresses[0].countryName)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startLocationUpdates() {
        locationTracker.setLocationListener { location ->
            updateUserLocation(location.latitude, location.longitude)
            updateCurrentLocation(location.latitude, location.longitude)
            val locationRequest = LocationRequest(
                longitude = location.longitude.toString(),
                latitude = location.latitude.toString(),
                figureType = "CIRCLE",
                place = _mapState.value.currentLocationName
            )

            // todo return on release
//            if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//                    !location.isMock
//                } else {
//                    !location.isFromMockProvider
//                }
//            )
            sendLocationToServer(locationRequest)
        }
        locationTracker.startTracking()

    }

    private fun sendLocationToServer(locationRequest: LocationRequest) {
        webSocketClient.sendLocationRequest(locationRequest)
    }


    private fun getTheme() {
        _mapState.update { state -> state.copy(isDarkTheme = themePreferenceManager.isDarkTheme()) }
    }

    fun updateTheme(isDarkTheme: Boolean) {
        themePreferenceManager.setDarkTheme(isDarkTheme)
        _mapState.update { state -> state.copy(isDarkTheme = isDarkTheme) }
    }

    fun getStartData() {
        updateUiState(UiState.Loading)
        viewModelScope.launch {
            try {
                getQuests()
                getCoins()
                getBalance()
                loadFriendStatistics()
                fetchProfile()
                startWebSockets()
                getTheme()
                fetchAllNotes()
                getPrivacy()
//                updateUiState(UiState.Default)
            } catch (e: Exception) {
                updateUiState(UiState.Error("Нет подключения к серверу"))
                Log.e("MapViewModel", "Error getting start data", e)
            } finally {
                Log.d("MapViewModel", "Data loaded")
            }
        }
    }

    private suspend fun getQuests(addNew: Boolean = false) {
        try {
            val quests = getQuestsUseCase.execute()
            if (addNew) {
                _mapState.update {
                    it.copy(
                        notCompletedQuests = it.notCompletedQuests + quests.notCompleted,
                        completedQuests = quests.completed,
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
        latitude: Double, longitude: Double, radiusInMeters: Double
    ): List<List<Point>> {
        val earthRadius = 6378137.0
        val numberOfSides = 100
        val deltaLat = Math.toDegrees(radiusInMeters / earthRadius)
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

    fun getCorrectTransportType(transportType: TransportType): String {
        return when (transportType) {
            TransportType.WALK -> "пешком"
            TransportType.CAR -> "на машине"
            TransportType.BICYCLE -> "на велосипеде"
        }
    }

    fun getCorrectDifficulty(difficulty: DifficultyType): String {
        return when (difficulty) {
            DifficultyType.EASY -> "легкий"
            DifficultyType.MEDIUM -> "средняя сложность"
            DifficultyType.HARD -> "сложный"
        }
    }

    fun getColorByDifficulty(difficulty: DifficultyType): Color {
        return when (difficulty) {
            DifficultyType.EASY -> Green
            DifficultyType.MEDIUM -> Yellow
            DifficultyType.HARD -> Red
        }
    }

    private fun onInnerListUpdate(newInnerPoints: List<LineString>) {
        _mapState.update { it.copy(innerPoints = newInnerPoints) }
    }

    private fun observeWebSocketConnection() {
        viewModelScope.launch {
            webSocketClient.isConnected.collect { isConnected ->
                if (isConnected == true) {
                    updateUiState(UiState.Default)
                } else {
                    updateUiState(UiState.Error("Нет подключения к серверу"))
                }
            }
        }
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

    private suspend fun loadFriendStatistics() {
        try {
            val friendStats = getFriendStatisticUseCase.execute()
            val friendLocations = friendStats.associate {
                it.profileDto.userId to ((it.previousLatitude?.toDouble()
                    ?: 0.0) to (it.previousLongitude?.toDouble()
                    ?: 0.0))
            }

            val friendAvatars = friendStats.associate { friendStat ->
                friendStat.profileDto.userId to Pair(
                    loadImage(friendStat.profileDto.inventoryDto.avatarFrames?.url), loadImage(friendStat.profileDto.avatarUrl)
                )
            }
            _mapState.update { state ->
                state.copy(
                    friendsLocations = friendLocations, friendAvatars = friendAvatars
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun observeEventWebSocketMessages() {
        viewModelScope.launch {
            eventWebSocketClient.events.collect { event ->
                event.let {
                    when (it.type) {
                        EventType.COMPLETE_QUEST -> {
                            Log.d("MapViewModel", "Quest completed")
                            _mapState.update { state ->
                                state.copy(
                                    event = it,
                                    activeQuest = null,
                                    p2pQuest = null,
                                    distanceQuest = null
                                )
                            }
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
                            val info = it.text.split(";")
                            state.copy(
                                event = it,
                                userBalance = state.userBalance?.copy(
                                    level = info[0].toInt(),
                                    totalExperienceInLevel = info[1].toInt()
                                )
                            )
                        }

                        EventType.UPDATE_EXPERIENCE -> _mapState.update { state ->
                            state.copy(
                                userBalance = state.userBalance?.copy(experience = it.text.toInt())
                            )
                        }

                        EventType.UPDATE_BATTLE_PASS_LEVEL -> {

                        }
                    }
                }
            }
        }
    }


    private suspend fun loadImage(imageUrl: String?): Bitmap? {
        return try {
            if (imageUrl.isNullOrEmpty()) {
                null
            } else {
                withContext(Dispatchers.IO) {
                    Glide.with(context).asBitmap().load(imageUrl).submit().get()
                }
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

    private fun onFriendsLocationUpdate(friendLocation: CreatePolygonRequestDto) {
        _mapState.update { state ->
            val updatedLocations = state.friendsLocations.toMutableMap()
            updatedLocations[friendLocation.userId] =
                friendLocation.createPolygonRequestDto.latitude to
                        friendLocation.createPolygonRequestDto.longitude
            state.copy(friendsLocations = updatedLocations)
        }
    }


    private suspend fun getBalance() {
        try {
            val result = getBalanceUseCase.execute()
            _mapState.update { it.copy(userBalance = result) }
        } catch (e: Exception) {
            Log.e("BalanceViewModel", "Failed to fetch balance", e)

        }
    }

    public override fun onCleared() {
        super.onCleared()
        webSocketClient.close()
        friendsLocationWebSocketClient.close()
    }

    private fun onAreaUpdate(areaPercent: Double) {
        _mapState.update { it.copy(currentLocationPercent = areaPercent) }
    }

    fun updateUiState(uiState: UiState) {
        _mapState.update { it.copy(uiState = uiState) }
    }

    fun updateShowViewAnnotationIndex(index: Int?) {
        _mapState.update { it.copy(showViewAnnotationIndex = index) }
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

    fun updateInfoText(text: String?) {
        _mapState.update { it.copy(infoText = text) }
    }

    fun updateShopOpen() {
        _mapState.update { it.copy(isShopOpen = !it.isShopOpen) }
//        fetchBalance()
    }

    fun onFriendMarkerClicked(friendId: String) {
        viewModelScope.launch {
            try {
                val friendPolygons = polygonRepository.getFriendPolygons(friendId)
                val friendProfile = FriendProfile(id = friendId,
                    polygons = friendPolygons.features.flatMap { feature ->
                        feature.geometry.coordinates.flatMap { coordinateList ->
                            coordinateList.map { innerList ->
                                innerList.map { coordinates ->
                                    Point.fromLngLat(coordinates[0], coordinates[1])
                                }
                            }
                        }
                    })
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
        // todo event on this update
        viewModelScope.launch {
            fetchProfile()
            _mapState.update { it.copy(isInventoryOpen = !it.isInventoryOpen) }
        }
    }

    fun updateBattlePassOpenScreen() {
        _mapState.update { it.copy(isBattlePassOpen = !it.isBattlePassOpen) }
    }

    fun createNote(text: String, list: List<Uri>) {
        val node = NoteMultipart(
            text = text,
            latitude = _mapState.value.createNotePoint?.latitude().toString(),
            longitude = _mapState.value.createNotePoint?.longitude().toString(),
            images = list
        )
        viewModelScope.launch {
            try {
                createNoteUseCase.execute(node)
                fetchAllNotes()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateCreateNoteScreen(point: Point?) {
        _mapState.update { it.copy(createNotePoint = point) }
    }

    private suspend fun fetchAllNotes() {
        try {
            val notes = getAllNotesUseCase.execute()
            _mapState.update {
                it.copy(noteList = notes)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun openNoteById(noteId: Long) {
        viewModelScope.launch {
            try {
                val note = getNoteUseCase.execute(noteId)
                _mapState.update {
                    it.copy(note = it.note?.copy(note = note))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateNote(note: MapNote?) {
        _mapState.update { it.copy(note = note) }
    }

    fun updateLeaderboardOpen() {
        _mapState.update { it.copy(isLeaderboardOpen = !it.isLeaderboardOpen) }
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

    fun updateCompletedQuestScreen(){
        _mapState.update { it.copy(isCompletedQuestOpen = !it.isCompletedQuestOpen) }
    }

    private suspend fun getPrivacy() {
        val privacy = statisticRepository.getPrivacy()
        _mapState.update { it.copy(isPublicPrivacy = privacy.isPublic) }
    }

    fun setPrivacy(isPublic: Boolean) {
        viewModelScope.launch {
            try {
                statisticRepository.setPrivacy(isPublic)
                _mapState.update { it.copy(isPublicPrivacy = isPublic) }
//                getPrivacy()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}



