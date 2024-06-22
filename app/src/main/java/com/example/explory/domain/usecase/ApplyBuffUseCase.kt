package com.example.explory.domain.usecase

import com.example.explory.data.repository.BuffRepository

class ApplyBuffUseCase(
    private val buffRepository: BuffRepository
) {
    suspend fun execute(buffId: Long) = buffRepository.applyBuff(buffId)
}
