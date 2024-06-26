package com.example.explory.domain.usecase

import com.example.explory.data.model.quest.DistanceQuestDto
import com.example.explory.data.repository.QuestRepository

class GetDistanceQuestUseCase(
    private val questRepository: QuestRepository
) {
    suspend fun execute(questId: String): DistanceQuestDto {
        return questRepository.getDistanceQuest(questId)
    }
}