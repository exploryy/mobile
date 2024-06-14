package com.example.explory.domain.usecase

import com.example.explory.data.model.profile.FriendProfileDto
import com.example.explory.data.repository.FriendRepository

class GetFriendProfileUseCase(private val repository: FriendRepository) {
    suspend operator fun invoke(clientId: String): FriendProfileDto {
        return repository.getFriendProfile(clientId)
    }
}
