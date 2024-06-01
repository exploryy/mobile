package com.example.explory.domain.usecase

import com.example.explory.data.repository.ProfileRepository
import com.example.explory.data.service.ProfileDto

class GetProfileUseCase(
    private val profileRepository: ProfileRepository
) {
    suspend fun execute(): ProfileDto {
        return profileRepository.getProfile()
    }
}