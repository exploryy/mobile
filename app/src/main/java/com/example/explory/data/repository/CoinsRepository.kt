package com.example.explory.data.repository

import com.example.explory.data.service.ApiService
import com.example.explory.data.model.CoinDto

class CoinsRepository(private val apiService: ApiService) {
    suspend fun getCoins(): List<CoinDto> {
        return apiService.getCoins()
    }
}
