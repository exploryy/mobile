package com.example.explory.data.repository

import com.example.explory.data.model.leaderboard.TotalStatisticDto
import com.example.explory.data.service.ApiService

class StatisticRepository(private val apiService: ApiService) {
    suspend fun getExperienceStatistic(count: Int) : TotalStatisticDto{
        return apiService.getExperienceStatistic(count)
    }

    suspend fun getDistanceStatistic(count: Int): TotalStatisticDto{
        return apiService.getDistanceStatistic(count)
    }

    suspend fun setPrivacy(isPublic: Boolean) {
        apiService.setPrivacy(isPublic)
    }
}