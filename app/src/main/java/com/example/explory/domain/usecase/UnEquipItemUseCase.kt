package com.example.explory.domain.usecase

import com.example.explory.data.repository.InventoryRepository

class UnEquipItemUseCase(private val repository: InventoryRepository) {
    suspend fun execute(itemId: Long) {
        repository.unEquipItem(itemId)
    }
}