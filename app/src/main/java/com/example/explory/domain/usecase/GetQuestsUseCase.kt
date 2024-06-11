package com.example.explory.domain.usecase

import com.example.explory.data.model.quest.QuestListDto
import com.example.explory.data.repository.QuestRepository

class GetQuestsUseCase(private val questRepository: QuestRepository) {
    suspend fun ex ecute(): QuestListDto {
        return questRepository.getQuests()
    }
}
