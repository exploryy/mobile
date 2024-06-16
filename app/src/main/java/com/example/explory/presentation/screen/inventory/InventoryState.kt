package com.example.explory.presentation.screen.inventory

import com.example.explory.data.model.inventory.CosmeticItemInInventoryDto

data class InventoryState(
    val inventoryList: List<CosmeticItemInInventoryDto> = emptyList(),
    val selectedItem: CosmeticItemInInventoryDto? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isOpenDescriptionDialog: Boolean = false
)
