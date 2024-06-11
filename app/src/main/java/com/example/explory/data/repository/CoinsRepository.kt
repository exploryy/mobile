package com.example.explory.data.repository

import com.example.explory.data.model.CoinDto
import com.example.explory.data.service.ApiService

class CoinsRepository(private val apiService: ApiService) {
    suspend fun getCoins(): List<CoinDto> {
        return apiService.getCoins()
    }

    suspend fun collectCoin(coinId: Long) {
        apiService.consumeCoin(coinId)
    }
}
