package com.example.explory.presentation.screen.inventory

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
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

@SuppressLint("StaticFieldLeak")
class InventoryViewModel(
    private val getInventoryUseCase: GetInventoryUseCase,
    private val equipItemUseCase: EquipItemUseCase,
    private val unEquipItemUseCase: UnEquipItemUseCase,
    private val sellItemUseCase: SellItemUseCase,
    private val context: Context
) : ViewModel() {
    private val _inventoryState = MutableStateFlow(InventoryState())
    val inventoryState: StateFlow<InventoryState> = _inventoryState

    init {
        fetchInventory()
    }

    val iconsList = listOf(2L)

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
                closeItemDialog()
                Toast.makeText(context, "Предмет экипирован", Toast.LENGTH_SHORT).show()
                if (iconsList.contains(itemId)){
                    setAppIcon(itemId)
                }
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
                closeItemDialog()
                Toast.makeText(context, "Предмет снят", Toast.LENGTH_SHORT).show()
                if (iconsList.contains(itemId)){
                    setAppIcon(null)
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

    private fun setAppIcon(iconId: Long?) {
        Log.d("ИКОНКА", iconId.toString())
        val pm = context.packageManager

        pm.setComponentEnabledSetting(
            ComponentName(context, "com.example.explory.MainActivity"),
            if (iconId == null) PackageManager.COMPONENT_ENABLED_STATE_ENABLED else PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )

        pm.setComponentEnabledSetting(
            ComponentName(context, "com.example.explory.MainActivityAlias1"),
            if (iconId == 4L) PackageManager.COMPONENT_ENABLED_STATE_ENABLED else PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
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
