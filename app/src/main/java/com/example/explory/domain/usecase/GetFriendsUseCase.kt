package com.example.explory.domain.usecase

import com.example.explory.data.model.friend.FriendsResponse
import com.example.explory.data.repository.FriendRepository

class GetFriendsUseCase(private val friendRepository: FriendRepository) {
    suspend fun execute(): FriendsResponse {
        return friendRepository.getFriends()
    }
}