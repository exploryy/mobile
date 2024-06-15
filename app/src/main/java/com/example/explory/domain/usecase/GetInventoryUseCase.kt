package com.example.explory.domain.usecase

import com.example.explory.data.model.inventory.CosmeticItemInInventoryDto
import com.example.explory.data.repository.InventoryRepository

class GetInventoryUseCase(private val repository: InventoryRepository) {
    suspend fun execute(): List<CosmeticItemInInventoryDto> {
        return repository.getInventory()
    }
}