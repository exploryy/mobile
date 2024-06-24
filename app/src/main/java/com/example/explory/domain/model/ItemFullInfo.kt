package com.example.explory.domain.model

import com.example.explory.data.model.shop.RarityType

data class ItemFullInfo(
    val itemId: Long,
    val imageUrl: String,
    val name: String,
    val description: String,
    val rarity: RarityType,
    val price: Int,
)