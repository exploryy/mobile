package com.example.explory.presentation.screen.quest

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explory.data.repository.QuestRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuestViewModel(
    private val questRepository: QuestRepository
) : ViewModel() {
    private val _state = MutableStateFlow(QuestState())
    val state = _state.asStateFlow()

    fun loadQuest(questId: String, questType: String) {
        when (questType) {
            "DISTANCE" -> loadDistanceQuest(questId)
            "POINT_TO_POINT" -> loadPointToPointQuest(questId)
        }
    }

    private fun loadDistanceQuest(questId: String) {
        _state.value = QuestState(isLoading = true)
        viewModelScope.launch {
            try {
                val distanceQuest = questRepository.getDistanceQuest(questId)
                _state.value = QuestState(distanceQuest = distanceQuest, isLoading = false)
            } catch (e: Exception) {
                Log.d("QuestViewModel", "Error loading distance quest", e)
                _state.value = QuestState(isLoading = false)
            }
        }
    }

    private fun loadPointToPointQuest(questId: String) {
        _state.value = QuestState(isLoading = true)
        viewModelScope.launch {
            try {
                val pointToPointQuest = questRepository.getP2PQuest(questId)
                _state.value = QuestState(pointToPointQuest = pointToPointQuest, isLoading = false)
            } catch (e: Exception) {
                Log.d("QuestViewModel", "Error loading point to point quest", e)
                _state.value = QuestState(isLoading = false)
            }
        }
    }
}
