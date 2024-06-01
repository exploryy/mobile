package com.example.explory.domain.usecase

import com.example.explory.data.repository.PolygonRepository
import com.example.explory.data.service.PolygonDto

class GetPolygonsUseCase(private val repository: PolygonRepository) {
    suspend fun execute(): PolygonDto {
        return repository.getPolygons()
    }
}
