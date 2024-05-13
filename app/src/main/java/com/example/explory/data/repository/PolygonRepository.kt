package com.example.explory.data.repository

import android.util.Log
import com.example.explory.data.service.GeoJsonResponse
import com.example.explory.data.service.OpenStreetMapService
import com.example.explory.presentation.utils.UiState

class PolygonRepository(private val openStreetMapService: OpenStreetMapService) {
    suspend fun getPolygons(result: (UiState) -> Unit): GeoJsonResponse? {
        try {
            val response = openStreetMapService.getWordContent()
            Log.d("PolygonRepository", "response: $response")
            return if (response.isSuccessful) {
                result(UiState.Success)
                response.body()
            } else {
                result(UiState.Error("Error"))
                null
            }
        } catch (e: Exception) {
            Log.d("PolygonRepository", "error: ${e.message}")
            result(UiState.Error(e.message ?: "Error"))
        }
        return null
    }
}
