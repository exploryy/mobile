package com.example.explory.domain.usecase

import com.example.explory.data.model.shop.CosmeticItemInShopDto
import com.example.explory.data.repository.ShopRepository

class GetShopUseCase(private val shopRepository: ShopRepository) {
    suspend fun execute() : List<CosmeticItemInShopDto>{
        return shopRepository.getShop()
    }
}