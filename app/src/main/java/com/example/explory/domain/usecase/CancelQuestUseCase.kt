package com.example.explory.domain.usecase

import com.example.explory.data.repository.QuestRepository

class CancelQuestUseCase(
    private val questRepository: QuestRepository
) {
    suspend fun execute(questId: String) {
        questRepository.cancelQuest(questId)
    }
}