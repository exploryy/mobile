package com.example.explory.data.repository

import com.example.explory.data.service.ApiService
import com.example.explory.data.service.DistanceQuestDto
import com.example.explory.data.service.PointToPointQuestDto
import com.example.explory.data.service.QuestDto

class QuestRepository(private val apiService: ApiService) {
    suspend fun getQuests(): List<QuestDto> {
        return apiService.getQuests()
    }

    suspend fun getP2PQuest(id: String): PointToPointQuestDto {
        return apiService.getPointToPointQuest(id)
    }

    suspend fun getDistanceQuest(id: String): DistanceQuestDto {
        return apiService.getDistanceQuest(id)
    }

    suspend fun startQuest(id: String, transportType: String) {
        apiService.startQuest(id, transportType)
    }
}
