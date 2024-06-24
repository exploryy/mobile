package com.example.explory.presentation.screen.battlepass

import com.example.explory.data.model.battlepass.BattlePassDto
import com.example.explory.data.model.statistic.UserStatisticDto

data class BattlePassState(
    val battlePass: BattlePassDto? = null,
    val userStatisticDto: UserStatisticDto? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)