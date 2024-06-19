package com.example.explory.data.repository

import com.example.explory.data.model.battlepass.BattlePassDto
import com.example.explory.data.service.ApiService

class BattlePassRepository(private val apiService: ApiService) {
    suspend fun getCurrentBattlePass() : BattlePassDto {
        return apiService.getCurrentBattlePass()
    }
}