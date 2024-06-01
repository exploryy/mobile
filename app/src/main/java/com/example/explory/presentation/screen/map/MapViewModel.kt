package com.example.explory.presentation.screen.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explory.data.model.location.LocationRequest
import com.example.explory.domain.usecase.GetPolygonsUseCase
import com.example.explory.domain.websocket.LocationTracker
import com.example.explory.domain.websocket.LocationWebSocketClient
import com.example.explory.presentation.utils.UiState
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Random

class MapViewModel(
    private val getPolygonsUseCase: GetPolygonsUseCase,
    private val webSocketClient: LocationWebSocketClient,
    private val locationTracker: LocationTracker
) : ViewModel() {
    private val _mapState = MutableStateFlow(MapState())
    val mapState = _mapState.asStateFlow()
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

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
        webSocketClient.connect()
        startLocationUpdates()
        observeWebSocketMessages()
    }

    private fun startLocationUpdates() {
        locationTracker.setLocationListener { location ->
            sendLocationToServer(location.latitude, location.longitude)
        }
        locationTracker.startTracking()
    }

    private fun sendLocationToServer(latitude: Double, longitude: Double) {
        val locationRequest = LocationRequest(
            longitude = longitude.toString(),
            latitude = latitude.toString(),
            figureType = "CIRCLE"
        )
        webSocketClient.sendLocationRequest(locationRequest)
    }

//    private fun launchPositionUpdates() {
//        uiScope.launch {
//            while (true) {
//                onInnerListUpdate(createRandomPointsList().map { LineString.fromLngLats(it) })
//                delay(5000L)
//            }
//        }
//    }

    private fun createRandomPointsList(): List<List<Point>> {
        val random = Random()
        val points = mutableListOf<Point>()
        val firstLast = Point.fromLngLat(
            random.nextDouble() * -360.0 + 180.0, random.nextDouble() * -180.0 + 90.0
        )
        points.add(firstLast)
        for (i in 0 until random.nextInt(322) + 4) {
            points.add(
                Point.fromLngLat(
                    random.nextDouble() * -360.0 + 180.0, random.nextDouble() * -180.0 + 90.0
                )
            )
        }
        points.add(firstLast)
        return listOf(points)
    }

    private fun updateUiState(state: UiState) {
        _mapState.update { mapState ->
            mapState.copy(uiState = state)
        }
    }

    private fun onInnerListUpdate(newInnerPoints: List<LineString>) {
        _mapState.update { state ->
            state.copy(
                innerPoints = newInnerPoints
            )
        }
    }

    private fun observeWebSocketMessages() {
        viewModelScope.launch {
            webSocketClient.messages.collect { response ->
                response?.let {
                    val newInnerPoints = it.geo.features.flatMap { feature ->
                        feature.geometry.coordinates.map { coordinateList ->
                            LineString.fromLngLats(
                                coordinateList.map { coordinates ->
                                    Point.fromLngLat(coordinates[0], coordinates[1])
                                }
                            )
                        }
                    }
                    onInnerListUpdate(newInnerPoints)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        webSocketClient.close()
        viewModelJob.cancel()
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
}



