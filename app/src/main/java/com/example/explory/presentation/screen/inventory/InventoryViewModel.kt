package com.example.explory.presentation.screen.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explory.data.model.inventory.CosmeticItemInInventoryDto
import com.example.explory.domain.usecase.EquipItemUseCase
import com.example.explory.domain.usecase.GetInventoryUseCase
import com.example.explory.domain.usecase.SellItemUseCase
import com.example.explory.domain.usecase.UnEquipItemUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InventoryViewModel(
    private val getInventoryUseCase: GetInventoryUseCase,
    private val equipItemUseCase: EquipItemUseCase,
    private val unEquipItemUseCase: UnEquipItemUseCase,
    private val sellItemUseCase: SellItemUseCase
) : ViewModel() {
    private val _inventoryState = MutableStateFlow(InventoryState())
    val inventoryState: StateFlow<InventoryState> = _inventoryState

    init {
        fetchInventory()
    }

    fun fetchInventory() {
        viewModelScope.launch {
            _inventoryState.update { it.copy(isLoading = true) }
            try {
                val items = getInventoryUseCase.execute()
                _inventoryState.update { it.copy(inventoryList = items, isLoading = false) }
            } catch (e: Exception) {
                _inventoryState.update { it.copy(errorMessage = e.message, isLoading = false) }
            }
        }
    }

    fun equipItem(itemId: Long) {
        viewModelScope.launch {
            _inventoryState.update { it.copy(isLoading = true) }
            try {
                equipItemUseCase.execute(itemId)
                fetchInventory()
            } catch (e: Exception) {
                _inventoryState.update { it.copy(errorMessage = e.message, isLoading = false) }
            }
        }
    }

    fun unEquipItem(itemId: Long) {
        viewModelScope.launch {
            _inventoryState.update { it.copy(isLoading = true) }
            try {
                unEquipItemUseCase.execute(itemId)
                fetchInventory()
            } catch (e: Exception) {
                _inventoryState.update { it.copy(errorMessage = e.message, isLoading = false) }
            }
        }
    }

    fun sellItem(itemId: Long) {
        viewModelScope.launch {
            _inventoryState.update { it.copy(isLoading = true) }
            try {
                sellItemUseCase.execute(itemId)
                fetchInventory()
            } catch (e: Exception) {
                _inventoryState.update { it.copy(errorMessage = e.message, isLoading = false) }
            }
        }
    }

    fun selectItem(item: CosmeticItemInInventoryDto) {
        _inventoryState.update { it.copy(selectedItem = item) }
    }

    public override fun onCleared() {
        super.onCleared()
    }
}
