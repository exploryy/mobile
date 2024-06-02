package com.example.explory.data.repository

import com.example.explory.data.service.ApiService
import com.example.explory.data.service.QuestDto

class QuestRepository(private val apiService: ApiService) {
    suspend fun getQuests(): List<QuestDto> {
        return apiService.getQuests()
    }
}
