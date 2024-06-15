package com.example.explory.domain.usecase

import com.example.explory.data.repository.InventoryRepository

class SellItemUseCase(private val repository: InventoryRepository) {
    suspend fun execute(itemId: Long) {
        repository.sellItem(itemId)
    }
}