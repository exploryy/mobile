package com.example.explory.domain.usecase

import com.example.explory.data.repository.PolygonRepository
import com.example.explory.presentation.map.UiState

class GetPolygonsUseCase(private val repository: PolygonRepository) {
    suspend fun execute(result: (UiState) -> Unit) = repository.getPolygons(result)
}
