package com.example.explory.data.model.battlepass

data class BattlePassLevelDto(
    val level: Int,
    val experienceNeeded: Int,
    val rewards: List<BattlePassRewardDto>
)
