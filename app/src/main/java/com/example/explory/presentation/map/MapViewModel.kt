package com.example.explory.presentation.map

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explory.data.service.GeoJsonResponse
import com.example.explory.domain.usecase.GetPolygonsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MapViewModel(private val getPolygonsUseCase: GetPolygonsUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Default)
    val uiState = _uiState.asStateFlow()
    private val _mapState = MutableStateFlow(MapState())
    val mapState = _mapState.asStateFlow()

    init {
        getTestPolygons()
    }

    private fun getTestPolygons() {
        _uiState.value = UiState.Loading
        Log.d("MapViewModel", "getTestPolygons")
        try {
            viewModelScope.launch {
                val response = getPolygonsUseCase.execute {
                    Log.d("MapViewModel", "getPolygonsUseCase result is $it")

                    _uiState.value = it
                }
                _mapState.value = MapState(response?.coordinates)
            }
        } finally {
            _uiState.value = UiState.Default
        }
    }
}


data class MapState(
    val polygons: List<List<List<List<Double>>>>? = null
)

sealed interface UiState {
    data object Default : UiState
    data object Loading : UiState
    data class Error(val message: String) : UiState
    data object Success : UiState
}