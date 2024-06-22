package com.example.explory.data.repository

import com.example.explory.data.service.ApiService

class BuffRepository(private val apiService: ApiService) {
    suspend fun getBuffList(level: Int) = apiService.getBuffList(level)

    suspend fun getMyBuffs() = apiService.getMyBuffs()

    suspend fun applyBuff(buffId: Long) = apiService.applyBuff(buffId)
}