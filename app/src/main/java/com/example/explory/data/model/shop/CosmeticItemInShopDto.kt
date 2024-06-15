package com.example.explory.data.model.shop

data class CosmeticItemInShopDto(
    val itemId: Long,
    val name: String,
    val description: String,
    val price: Int,
    val rarityType: RarityType,
    val cosmeticType: CosmeticType,
    val isOwned: Boolean,
    val sellable: Boolean,
    val url: String
)
