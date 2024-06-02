package com.example.explory.domain.usecase

import com.example.explory.data.model.profile.ProfileDto
import com.example.explory.data.repository.FriendRepository

class GetUserListUseCase(
    private val friendRepository: FriendRepository
) {
    suspend fun execute(username: String) : List<ProfileDto>{
        return friendRepository.getUserList(username)
    }
}