package com.example.explory.domain.usecase

import com.example.explory.data.model.statistic.Privacy
import com.example.explory.data.repository.StatisticRepository

class GetPrivacyUseCase(
    private val statisticRepository: StatisticRepository
) {
    suspend fun execute(): Privacy {
        return statisticRepository.getPrivacy()
    }
}
