package com.example.explory.presentation.screen.map

import androidx.lifecycle.ViewModel
import com.example.explory.domain.state.MapUiState
import com.example.explory.domain.usecase.GetPolygonsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.example.explory.data.model.GeoJson
import com.example.explory.data.model.location.LocationRequest
import com.example.explory.data.model.location.LocationResponse
import com.example.explory.domain.websocket.LocationTracker
import com.example.explory.domain.websocket.LocationWebSocketClient
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class MapViewModel(
    private val getPolygonsUseCase: GetPolygonsUseCase,
    private val webSocketClient: LocationWebSocketClient,
    private val locationTracker: LocationTracker
) : ViewModel() {
    private val _mapState = MutableStateFlow(MapState())
    val mapState = _mapState.asStateFlow()

    private val _mapUiState = MutableStateFlow(MapUiState())
    val mapUiState = _mapUiState.asStateFlow()

    init {
        webSocketClient.connect()
        startLocationUpdates()
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

    private fun onWebSocketResponse(response: LocationResponse) {
        val polygons = parseGeoJson(response.geo)
        _mapState.update { state ->
            state.copy(
                polygons = polygons
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




