package com.example.explory.presentation.screen.shop

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explory.data.model.shop.CosmeticItemInShopDto
import com.example.explory.domain.usecase.BuyItemUseCase
import com.example.explory.domain.usecase.GetBalanceUseCase
import com.example.explory.domain.usecase.GetShopUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@SuppressLint("StaticFieldLeak")
class ShopViewModel(
    private val getShopUseCase: GetShopUseCase,
    private val getBalanceUseCase: GetBalanceUseCase,
    private val buyItemUseCase: BuyItemUseCase,
    private val context: Context
) : ViewModel() {
    private val _shopState = MutableStateFlow(ShopState())
    val shopState: StateFlow<ShopState> = _shopState

//    init {
//        fetchShopItems()
//        fetchBalance()
//    }

    fun fetchShopItems() {
        viewModelScope.launch {
            try {
                val items = getShopUseCase.execute()
                _shopState.update { it.copy(shopList = items) }
            } catch (e: Exception) {
                // Handle the error
            }
        }
    }

    fun fetchBalance() {
        viewModelScope.launch {
            try {
                val balance = getBalanceUseCase.execute()
                _shopState.update { it.copy(balance = balance.balance) }
            } catch (e: Exception) {
                // Handle the error
            }
        }
    }

    fun buyItem(item: CosmeticItemInShopDto) {
        if (item.price > _shopState.value.balance) {
            Toast.makeText(context, "Вам не хватает денег, чтобы купить это", Toast.LENGTH_SHORT).show()
            return
        } else {
            viewModelScope.launch {
                try {
                    buyItemUseCase.execute(item.itemId)
                    fetchBalance()
                    fetchShopItems()
                    dismissDialog()
                    Toast.makeText(context, "Товар куплен", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    // Handle the error
                }
            }
        }
    }

    fun selectCategory(category: String) {
        _shopState.update { it.copy(selectedCategory = category) }
    }

    fun selectItem(item: CosmeticItemInShopDto) {
        _shopState.update { it.copy(selectedItem = item, isDialogVisible = true) }
    }

    fun dismissDialog() {
        _shopState.update { it.copy(isDialogVisible = false) }
    }

    public override fun onCleared() {
        super.onCleared()
    }
}