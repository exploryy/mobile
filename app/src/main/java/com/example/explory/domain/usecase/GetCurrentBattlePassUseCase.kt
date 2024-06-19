package com.example.explory.domain.usecase

import com.example.explory.data.model.battlepass.BattlePassDto
import com.example.explory.data.repository.BattlePassRepository

class GetCurrentBattlePassUseCase(private val battlePassRepository: BattlePassRepository) {
    suspend fun execute() : BattlePassDto {
        return battlePassRepository.getCurrentBattlePass()
    }
}