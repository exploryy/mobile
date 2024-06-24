package com.example.explory.presentation.screen.battlepass

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explory.domain.usecase.GetCurrentBattlePassUseCase
import com.example.explory.domain.usecase.GetUserStatisticUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BattlePassViewModel(
    private val getCurrentBattlePassUseCase: GetCurrentBattlePassUseCase,
    private val getUserStatisticUseCase: GetUserStatisticUseCase
) : ViewModel() {
    private val _battlePassState = MutableStateFlow(BattlePassState())
    val battlePassState: StateFlow<BattlePassState> = _battlePassState.asStateFlow()

    fun loadBattlePass() {
        viewModelScope.launch {
            _battlePassState.value = _battlePassState.value.copy(isLoading = true)
            try {
                val battlePass = getCurrentBattlePassUseCase.execute()
                _battlePassState.value = _battlePassState.value.copy(
                    battlePass = battlePass,
                    isLoading = false
                )
            } catch (e: Exception) {
                _battlePassState.value = _battlePassState.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }

    fun loadStatistic() {
        viewModelScope.launch {
            _battlePassState.value = _battlePassState.value.copy(isLoading = true)
            try {
                val statistic = getUserStatisticUseCase.execute()
                _battlePassState.value = _battlePassState.value.copy(
                    userStatisticDto = statistic,
                    isLoading = false
                )
            } catch (e: Exception) {
                _battlePassState.value = _battlePassState.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }
}
