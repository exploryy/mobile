package com.example.explory.presentation.screen.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import java.util.Random

class MapViewModel(private val getPolygonsUseCase: GetPolygonsUseCase) : ViewModel() {
    private val _mapState = MutableStateFlow(MapState())
    val mapState = _mapState.asStateFlow()

    private val _mapUiState = MutableStateFlow(MapUiState())
    val mapUiState = _mapUiState.asStateFlow()

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        launchPositionUpdates()
//        getTestPolygons()
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

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
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

    private fun getTestPolygons() {
        updateUiState(UiState.Loading)
        try {
            viewModelScope.launch {
                val response = getPolygonsUseCase.execute {
                    updateUiState(it)
                }
                _mapState.value = MapState(polygons = response?.coordinates)
            }
        } finally {
            updateUiState(UiState.Default)
        }
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



