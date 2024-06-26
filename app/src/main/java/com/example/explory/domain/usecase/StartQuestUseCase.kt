package com.example.explory.domain.usecase

import com.example.explory.data.model.quest.TransportType
import com.example.explory.data.repository.QuestRepository

class StartQuestUseCase(
    private val questRepository: QuestRepository
) {
    suspend fun execute(questId: String, transportType: TransportType) {
        questRepository.startQuest(questId, transportType)
    }
}