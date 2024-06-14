package com.example.explory.data.repository

import com.example.explory.data.model.quest.DistanceQuestDto
import com.example.explory.data.model.quest.PointToPointQuestDto
import com.example.explory.data.model.quest.QuestListDto
import com.example.explory.data.service.ApiService

class QuestRepository(private val apiService: ApiService) {
    suspend fun getQuests(): QuestListDto {
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

    suspend fun cancelQuest(questId: String) {
        apiService.cancelQuest(questId)
    }
}
