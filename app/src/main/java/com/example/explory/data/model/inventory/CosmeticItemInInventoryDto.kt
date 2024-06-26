package com.example.explory.data.model.inventory

import com.example.explory.data.model.shop.CosmeticType
import com.example.explory.data.model.shop.RarityType

data class CosmeticItemInInventoryDto(
    val itemId: Long,
    val name: String,
    val description: String,
    val price: Int,
    val rarityType: RarityType,
    val cosmeticType: CosmeticType,
    val isEquipped: Boolean,
    val url: String,
    val isSellable: Boolean
)
