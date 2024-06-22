package com.example.explory.data.model.quest

class CompletedQuestDto(
    val questId: Long,
    val name: String,
    val description: String,
    val difficultyType: DifficultyType,
    val questType: String,
    val transportType: TransportType,
    val latitude: String,
    val longitude: String,
    val images: List<String>,
    val startDate: String,
    val endDate: String
)
