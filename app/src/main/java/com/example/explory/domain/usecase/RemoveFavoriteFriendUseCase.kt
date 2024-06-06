package com.example.explory.domain.usecase

import com.example.explory.data.repository.FriendRepository

class RemoveFavoriteFriendUseCase(private val friendRepository: FriendRepository) {
    suspend fun execute(userId: String) {
        return friendRepository.removeFavoriteFriend(userId)
    }
}