package com.example.explory.domain.usecase

import com.example.explory.data.repository.CoinsRepository
import com.example.explory.data.model.CoinDto

class GetCoinsUseCase(private val coinsRepository: CoinsRepository) {
    suspend fun execute(): List<CoinDto> {
        return coinsRepository.getCoins()
    }
}