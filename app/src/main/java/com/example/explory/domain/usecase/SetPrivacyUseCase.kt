package com.example.explory.domain.usecase

import com.example.explory.data.repository.StatisticRepository

class SetPrivacyUseCase(
    private val statisticRepository: StatisticRepository
) {
    suspend fun execute(isPrivate: Boolean) {
        statisticRepository.setPrivacy(isPrivate)
    }
}
