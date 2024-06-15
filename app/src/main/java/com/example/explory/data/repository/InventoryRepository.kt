package com.example.explory.data.repository

import com.example.explory.data.model.inventory.CosmeticItemInInventoryDto
import com.example.explory.data.service.ApiService

class InventoryRepository(private val apiService: ApiService) {
    suspend fun equipItem(itemId: Long) {
        apiService.equipItem(itemId)
    }

    suspend fun unEquipItem(itemId: Long) {
        apiService.unEquipItem(itemId)
    }

    suspend fun getInventory(): List<CosmeticItemInInventoryDto> {
        return apiService.getInventory()
    }

    suspend fun sellItem(itemId: Long) {
        apiService.sellItem(itemId)
    }
}