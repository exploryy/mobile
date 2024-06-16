package com.example.explory.data.model.friend

import com.example.explory.data.model.profile.InventoryDto

data class Friend(
    val userId: String,
    val name: String,
    val email: String,
    val avatar: String?,
    val inventoryDto: InventoryDto,
    val isBestFriend: Boolean = false
)
