package com.example.explory.domain.usecase

import com.example.explory.data.model.location.PolygonDto
import com.example.explory.data.repository.PolygonRepository

class GetFriendPolygonUseCase(
    private val polygonRepository: PolygonRepository
) {
    suspend fun execute(friendId: String): PolygonDto {
        return polygonRepository.getFriendPolygons(friendId)
    }
}

