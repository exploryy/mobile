package com.example.explory.presentation.screen.userstatistic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explory.domain.usecase.GetUserStatisticUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserStatisticViewModel(
    private val getUserStatisticUseCase: GetUserStatisticUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(StatisticState())
    val state: StateFlow<StatisticState> = _state.asStateFlow()


    fun fetchUserStatistics() {
        viewModelScope.launch {
            _state.value = StatisticState(isLoading = true)
            try {
                val statistic = getUserStatisticUseCase.execute()
                _state.value = StatisticState(userStatisticDto = statistic)
            } catch (e: Exception) {
                _state.value = StatisticState(error = e.message)
            }
        }
    }
}