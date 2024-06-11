package com.example.explory.data.repository

import com.example.explory.data.service.ApiService
import com.example.explory.data.model.location.PolygonDto

class PolygonRepository(
    private val apiService: ApiService,
) {
    suspend fun getPolygons(): PolygonDto {
        return apiService.getPolygons()
    }
}
