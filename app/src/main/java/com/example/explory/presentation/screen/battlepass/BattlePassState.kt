package com.example.explory.presentation.screen.battlepass

import com.example.explory.data.model.battlepass.BattlePassDto

data class BattlePassState(
    val battlePass: BattlePassDto? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)