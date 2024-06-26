package com.example.explory.domain.usecase

import com.example.explory.data.model.quest.PointToPointQuestDto
import com.example.explory.data.repository.QuestRepository

class GetP2PQuestUseCase(
    private val questRepository: QuestRepository
) {
    suspend fun execute(questId: String): PointToPointQuestDto {
        return questRepository.getP2PQuest(questId)
    }
}
