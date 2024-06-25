package com.example.explory.domain.usecase

import com.example.explory.data.model.profile.ProfileDto
import com.example.explory.data.repository.ProfileRepository

class GetUserProfileUseCase(
    private val userRepository: ProfileRepository
) {
    suspend fun execute(userId: String): ProfileDto {
        return userRepository.getProfileById(userId)
    }
}
