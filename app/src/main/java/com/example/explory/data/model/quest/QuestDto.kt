package com.example.explory.data.model.quest

data class QuestDto(
    val questId: Long,
    val name: String,
    val description: String,
    val difficultyType: String,
    val questType: String,
    val transportType: String,
    val latitude: String,
    val longitude: String,
    val images: List<String>
)