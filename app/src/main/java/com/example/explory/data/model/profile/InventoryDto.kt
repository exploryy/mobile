package com.example.explory.data.model.profile

import com.example.explory.data.model.inventory.CosmeticItemInInventoryDto

data class InventoryDto (
    val footprint: CosmeticItemInInventoryDto?,
    val avatarFrames: CosmeticItemInInventoryDto?,
    val applicationImage: CosmeticItemInInventoryDto?,
    val fog: CosmeticItemInInventoryDto?
)
