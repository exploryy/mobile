package com.example.explory.domain.usecase

import com.example.explory.data.repository.QuestRepository
import com.example.explory.data.service.QuestDto

class GetQuestsUseCase(private val questRepository: QuestRepository) {
    suspend fun execute(): List<QuestDto> {
        return questRepository.getQuests()
    }
}
