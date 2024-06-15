package com.example.explory.domain.usecase

import com.example.explory.data.model.BalanceDto
import com.example.explory.data.repository.CoinsRepository

class GetBalanceUseCase(private val coinsRepository: CoinsRepository) {
    suspend fun execute() : BalanceDto{
        return coinsRepository.getBalance()
    }
}