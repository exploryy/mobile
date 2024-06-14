package com.example.explory.presentation.screen.shop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explory.data.model.shop.CosmeticItemInShopDto
import com.example.explory.domain.usecase.GetBalanceUseCase
import com.example.explory.domain.usecase.GetShopUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ShopViewModel(
    private val getShopUseCase: GetShopUseCase,
    private val getBalanceUseCase: GetBalanceUseCase
) : ViewModel() {
    private val _shopState = MutableStateFlow(ShopState())
    val shopState: StateFlow<ShopState> = _shopState

    init {
        fetchShopItems()
    }

    private fun fetchShopItems() {
        viewModelScope.launch {
            try {
                val items = getShopUseCase.execute()
                _shopState.value = _shopState.value.copy(shopList = items)
            } catch (e: Exception) {
                // Handle the error
            }
        }
    }

    fun selectCategory(category: String) {
        _shopState.update { it.copy(selectedCategory = category) }
    }

    fun selectItem(item: CosmeticItemInShopDto) {
        _shopState.value = _shopState.value.copy(selectedItem = item)
    }
}