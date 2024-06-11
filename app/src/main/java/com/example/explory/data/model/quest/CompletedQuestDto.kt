package com.example.explory.data.model.quest

import java.time.OffsetDateTime

class CompletedQuestDto(
    val questId: Long,
    val name: String,
    val description: String,
    val difficultyType: String,
    val questType: String,
    val transportType: String,
    val latitude: String,
    val longitude: String,
    val images: List<String>,
    val startDate: OffsetDateTime,
    val endDate: OffsetDateTime
)
