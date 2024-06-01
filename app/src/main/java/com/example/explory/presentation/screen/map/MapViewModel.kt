package com.example.explory.presentation.screen.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explory.data.model.GeoJson
import com.example.explory.domain.state.MapUiState
import com.example.explory.domain.usecase.GetPolygonsUseCase
import com.example.explory.presentation.utils.UiState
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.util.Random

class MapViewModel(private val getPolygonsUseCase: GetPolygonsUseCase) : ViewModel() {
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

    init {
        webSocketClient.connect()
        startLocationUpdates()
    }

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        launchPositionUpdates()
//        getTestPolygons()
    }

    private fun sendLocationToServer(latitude: Double, longitude: Double) {
        val locationRequest = LocationRequest(
            longitude = longitude.toString(),
            latitude = latitude.toString(),
            figureType = "CIRCLE"
        )
        webSocketClient.sendLocationRequest(locationRequest)
    }
    private fun launchPositionUpdates() {
        uiScope.launch {
            while (true) {
                onInnerListUpdate(createRandomPointsList().map { LineString.fromLngLats(it) })
                delay(5000L)
            }
        }
    }

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
    private fun onWebSocketResponse(response: LocationResponse) {
        val polygons = parseGeoJson(response.geo)
        _mapState.update { state ->
            state.copy(
                polygons = polygons
            )
        }
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

    private fun parseGeoJson(geoJson: String): List<List<List<Double>>> {
        val geoJsonObject = Json.decodeFromString<GeoJson>(geoJson)
        return geoJsonObject.coordinates
    }

    override fun onCleared() {
        super.onCleared()
        webSocketClient.close()
    }

    fun updateShowMap(show: Boolean) {
        _mapUiState.update { it.copy(showMap = show) }
    }

    fun updateShowRequestPermissionButton(show: Boolean) {
        _mapUiState.update { it.copy(showRequestPermissionButton = show) }
    }

    fun incrementPermissionRequestCount() {
        _mapUiState.update { it.copy(permissionRequestCount = it.permissionRequestCount + 1) }
    }

    fun updateShowFriendScreen(){
        _mapUiState.update { it.copy(showFriendsScreen = !it.showFriendsScreen) }
    }
}



