package com.example.explory.domain.usecase

import com.example.explory.data.model.statistic.UserStatisticDto
import com.example.explory.data.repository.ProfileRepository

class GetUserStatisticUseCase(
    private val profileRepository: ProfileRepository
) {
    suspend fun execute(): UserStatisticDto {
        return profileRepository.getUserStatistic()
    }
}