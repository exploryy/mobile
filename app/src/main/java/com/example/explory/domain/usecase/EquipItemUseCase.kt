package com.example.explory.domain.usecase

import com.example.explory.data.repository.InventoryRepository

class EquipItemUseCase(private val repository: InventoryRepository) {
    suspend fun execute(itemId: Long) {
        repository.equipItem(itemId)
    }
}