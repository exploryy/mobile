package com.example.explory.presentation.screen.inventory

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explory.data.model.inventory.CosmeticItemInInventoryDto
import com.example.explory.data.model.shop.CosmeticType
import com.example.explory.data.storage.ThemePreferenceManager
import com.example.explory.domain.usecase.EquipItemUseCase
import com.example.explory.domain.usecase.GetInventoryUseCase
import com.example.explory.domain.usecase.SellItemUseCase
import com.example.explory.domain.usecase.UnEquipItemUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@SuppressLint("StaticFieldLeak")
class InventoryViewModel(
    private val getInventoryUseCase: GetInventoryUseCase,
    private val equipItemUseCase: EquipItemUseCase,
    private val unEquipItemUseCase: UnEquipItemUseCase,
    private val sellItemUseCase: SellItemUseCase,
    private val sharedPreferenceManager: ThemePreferenceManager,
    private val context: Context
) : ViewModel() {
    private val _inventoryState = MutableStateFlow(InventoryState())
    val inventoryState: StateFlow<InventoryState> = _inventoryState

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

    fun equipItem(item: CosmeticItemInInventoryDto) {
        viewModelScope.launch {
            _inventoryState.update { it.copy(isLoading = true) }
            try {
                equipItemUseCase.execute(item.itemId)
                fetchInventory()
                closeItemDialog()
                Toast.makeText(context, "Предмет экипирован", Toast.LENGTH_SHORT).show()
                if (item.cosmeticType == CosmeticType.APPLICATION_IMAGE) {
                    changeIcon(item.name)
                }
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "Сначала снимите другой предмет этого типа",
                    Toast.LENGTH_SHORT
                ).show()
                _inventoryState.update { it.copy(errorMessage = e.message, isLoading = false) }
            }
        }
    }

    fun changeIcon(name: String) {
        when (name) {
            "Тик тик" -> {
                sharedPreferenceManager.setCurrentIcon("com.example.explory.MainActivityAliasTikTok")
            }

            "Уникум" -> {
                sharedPreferenceManager.setCurrentIcon("com.example.explory.MainActivityAliasTsu")
            }

//            "Главный спонсор" -> {
//                sharedPreferenceManager.setCurrentIcon("com.example.explory.MainActivityAliasSponsor")
//            }

            "Open the world" -> {
                sharedPreferenceManager.setCurrentIcon("com.example.explory.MainActivityAliasWorld")
            }


            else -> {
                sharedPreferenceManager.setCurrentIcon("com.example.explory.MainActivity")
            }
        }
    }

    fun unEquipItem(item: CosmeticItemInInventoryDto) {
        viewModelScope.launch {
            _inventoryState.update { it.copy(isLoading = true) }
            try {
                unEquipItemUseCase.execute(item.itemId)
                fetchInventory()
                closeItemDialog()
                Toast.makeText(context, "Предмет снят", Toast.LENGTH_SHORT).show()
                if (item.cosmeticType == CosmeticType.APPLICATION_IMAGE) {
                    changeIcon("")
                }
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
                closeItemDialog()
            } catch (e: Exception) {
                _inventoryState.update { it.copy(errorMessage = e.message, isLoading = false) }
            }
        }
    }

    fun openItemDialog(item: CosmeticItemInInventoryDto) {
        _inventoryState.update { it.copy(isOpenDescriptionDialog = true, selectedItem = item) }
    }

    fun closeItemDialog() {
        _inventoryState.update { it.copy(isOpenDescriptionDialog = false, selectedItem = null) }
    }

    public override fun onCleared() {
        super.onCleared()
    }
}