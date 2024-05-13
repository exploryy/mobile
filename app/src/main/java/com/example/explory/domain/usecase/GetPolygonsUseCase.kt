package com.example.explory.domain.usecase

import com.example.explory.data.repository.PolygonRepository
import com.example.explory.presentation.utils.UiState

class GetPolygonsUseCase(private val repository: PolygonRepository) {
    suspend fun execute(result: (UiState) -> Unit) = repository.getPolygons(result)
}
