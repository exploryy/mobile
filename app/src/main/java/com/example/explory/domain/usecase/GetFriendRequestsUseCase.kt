package com.example.explory.domain.usecase

import com.example.explory.data.model.requests.FriendRequest
import com.example.explory.data.repository.FriendRepository

class GetFriendRequestsUseCase(private val friendRepository: FriendRepository) {
    suspend fun execute(): FriendRequest {
        return friendRepository.getFriendRequests()
    }
}