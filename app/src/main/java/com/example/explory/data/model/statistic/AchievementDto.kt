package com.example.explory.data.model.statistic

import java.time.OffsetDateTime

data class AchievementDto(
    val achievementId: Long,
    val name: String,
    val description: String,
    val imageUrl: String,
    val isCompleted: Boolean,
    val completionDate: OffsetDateTime
)