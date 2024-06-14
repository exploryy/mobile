package com.example.explory.data.repository

import com.example.explory.data.model.location.PolygonDto
import com.example.explory.data.service.ApiService

class PolygonRepository(
    private val apiService: ApiService,
) {
    suspend fun getPolygons(): PolygonDto {
        return apiService.getPolygons()
    }

    suspend fun getFriendPolygons(friendId: String): PolygonDto {
        return apiService.getFriendPolygons(friendId)
    }
}
