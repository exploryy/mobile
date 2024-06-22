package com.example.explory.domain.usecase

import com.example.explory.data.repository.BuffRepository
import com.example.explory.presentation.screen.map.component.BuffResponse

class GetBuffListUseCase(private val buffRepository: BuffRepository) {
    suspend fun execute(): List<BuffResponse> = buffRepository.getBuffList()
}
