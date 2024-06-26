package com.example.explory.domain.usecase

import com.example.explory.data.repository.BuffRepository
import com.example.explory.data.model.event.BuffResponse

class GetBuffListUseCase(private val buffRepository: BuffRepository) {
    suspend fun execute(level: Int): List<BuffResponse> = buffRepository.getBuffList(level)
}
