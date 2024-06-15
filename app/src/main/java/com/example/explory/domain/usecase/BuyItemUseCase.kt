package com.example.explory.domain.usecase

import com.example.explory.data.repository.ShopRepository

class BuyItemUseCase(private val shopRepository: ShopRepository) {
    suspend fun execute(itemId: Long) {
        shopRepository.buyItem(itemId)
    }
}