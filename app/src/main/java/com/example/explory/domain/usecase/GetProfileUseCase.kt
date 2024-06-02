package com.example.explory.domain.usecase

import com.example.explory.data.model.profile.ProfileDto
import com.example.explory.data.repository.ProfileRepository

class GetProfileUseCase(
    private val profileRepository: ProfileRepository
) {
    suspend fun execute(): ProfileDto {
        return profileRepository.getProfile()
    }
}