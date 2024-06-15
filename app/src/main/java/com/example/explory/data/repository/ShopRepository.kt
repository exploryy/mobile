package com.example.explory.data.repository

import com.example.explory.data.model.shop.CosmeticItemInShopDto
import com.example.explory.data.service.ApiService

class ShopRepository(private val apiService: ApiService) {
    suspend fun getShop() : List<CosmeticItemInShopDto> {
        return apiService.getShop()
    }
}