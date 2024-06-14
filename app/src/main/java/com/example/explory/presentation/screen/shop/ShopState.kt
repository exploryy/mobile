package com.example.explory.presentation.screen.shop

import com.example.explory.data.model.shop.CosmeticItemInShopDto

data class ShopState(
    val shopList: List<CosmeticItemInShopDto> = emptyList(),
    val selectedItem: CosmeticItemInShopDto? = null,
    val selectedCategory: String = "Все"
)
