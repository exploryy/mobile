package com.example.explory.data.repository

import android.util.Log
import com.example.explory.data.model.CoinDto
import com.example.explory.data.service.ApiService

class CoinsRepository(private val apiService: ApiService) {
    suspend fun getCoins(): List<CoinDto> {
        return apiService.getCoins()
    }

    suspend fun collectCoin(coinId: Long) {
        Log.d("Repository", "Collect coin with id $coinId")
        apiService.consumeCoin(coinId)
    }
}
