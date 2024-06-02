package com.example.explory.domain.usecase

import com.example.explory.data.model.profile.ProfileMultipart
import com.example.explory.data.repository.ProfileRepository

class EditProfileUseCase(private val profileRepository: ProfileRepository) {
    suspend fun execute(profileRequest: ProfileMultipart) {
        profileRepository.editProfile(profileRequest)
    }
}