package com.example.explory.domain.usecase

import com.example.explory.data.model.location.LocationStatisticDto
import com.example.explory.data.repository.FriendRepository

class GetFriendStatisticUseCase(
    private val friendRepository: FriendRepository
) {
    suspend fun execute() : List<LocationStatisticDto> {
        return friendRepository.getFriendStatistic()
    }
}