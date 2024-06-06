package com.example.explory.presentation.screen.map

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explory.data.model.location.LocationRequest
import com.example.explory.domain.usecase.GetCoinsUseCase
import com.example.explory.domain.usecase.GetPolygonsUseCase
import com.example.explory.domain.usecase.GetQuestsUseCase
import com.example.explory.domain.websocket.LocationTracker
import com.example.explory.domain.websocket.LocationWebSocketClient
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

class MapViewModel(
    private val getPolygonsUseCase: GetPolygonsUseCase,
    private val getQuestsUseCase: GetQuestsUseCase,
    private val getCoinsUseCase: GetCoinsUseCase,
    private val webSocketClient: LocationWebSocketClient,
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
        startLocationUpdates()
        observeWebSocketMessages()
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
        locationTracker.setLocationListener { location ->
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses: List<Address>? =
                geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if (!addresses.isNullOrEmpty())
                if (addresses[0].locality != _mapState.value.currentLocationName) {
                    onCurrentLocationCityChanged(addresses[0].locality)
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
            _mapState.update { it ->
                it.copy(questPoints = quests.map {
                    Point.fromLngLat(it.longitude.toDouble(), it.latitude.toDouble())
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun getCoins() {
        try {
            val coins = getCoinsUseCase.execute()
            _mapState.update { it ->
                it.copy(coinPoints = coins.map {
                    Point.fromLngLat(it.latitude.toDouble(), it.longitude.toDouble())
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


//    private fun createRandomPointsList(): List<List<Point>> {
//        val random = Random()
//        val points = mutableListOf<Point>()
//        val firstLast = Point.fromLngLat(
//            random.nextDouble() * -360.0 + 180.0, random.nextDouble() * -180.0 + 90.0
//        )
//        points.add(firstLast)
//        for (i in 0 until random.nextInt(322) + 4) {
//            points.add(
//                Point.fromLngLat(
//                    random.nextDouble() * -360.0 + 180.0, random.nextDouble() * -180.0 + 90.0
//                )
//            )
//        }
//        points.add(firstLast)
//        return listOf(points)
//    }


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

    override fun onCleared() {
        super.onCleared()
        webSocketClient.close()
    }

    private fun onAreaUpdate(areaPercent: Double) {
        _mapState.update { it.copy(currentLocationPercent = areaPercent) }
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

    fun onCurrentLocationCityChanged(locality: String?) {
        _mapState.update { it.copy(currentLocationName = locality ?: "") }
    }
}



