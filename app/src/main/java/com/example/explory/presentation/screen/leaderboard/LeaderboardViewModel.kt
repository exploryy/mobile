package com.example.explory.presentation.screen.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explory.data.repository.StatisticRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LeaderboardViewModel(
    private val statisticRepository: StatisticRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LeaderboardState())
    val state: StateFlow<LeaderboardState> get() = _state

    fun setCurrentScreen(screen: Int) {
        _state.value = _state.value.copy(currentScreen = screen)
        when (screen) {
            1 -> fetchDistanceStatistic(10)
            2 -> fetchExperienceStatistic(10)
        }
    }

    private fun fetchExperienceStatistic(count: Int) {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            try {
                val statistic = statisticRepository.getExperienceStatistic(count)
                _state.value = _state.value.copy(
                    leadersCount = count,
                    leadersList = statistic,
                    isLoading = false
                )
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    private fun fetchDistanceStatistic(count: Int) {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            try {
                val statistic = statisticRepository.getDistanceStatistic(count)
                _state.value = _state.value.copy(
                    leadersCount = count,
                    leadersList = statistic,
                    isLoading = false
                )
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
