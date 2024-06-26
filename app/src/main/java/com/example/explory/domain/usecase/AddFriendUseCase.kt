package com.example.explory.domain.usecase

import com.example.explory.data.repository.FriendRepository

class AddFriendUseCase(private val friendRepository: FriendRepository) {
    suspend fun execute(userId: String) {
        return friendRepository.addFriend(userId)
    }
}