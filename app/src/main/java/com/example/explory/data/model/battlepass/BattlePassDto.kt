package com.example.explory.data.model.battlepass

data class BattlePassDto(
    val battlePassId: Long,
    val name: String,
    val description: String,
    val startDate: String,
    val endDate: String,
    val currentLevel: Int,
    val currentExperience: Int,
    val levels: List<BattlePassLevelDto>
)