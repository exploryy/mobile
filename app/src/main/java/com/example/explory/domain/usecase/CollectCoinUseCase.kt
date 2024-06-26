package com.example.explory.domain.usecase

import com.example.explory.data.repository.CoinsRepository

class CollectCoinUseCase(
    private val coinRepository: CoinsRepository
) {
    suspend fun execute(coinId: Long) {
        coinRepository.collectCoin(coinId)
    }
}
