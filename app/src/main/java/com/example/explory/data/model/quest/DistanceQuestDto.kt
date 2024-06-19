package com.example.explory.data.model.quest

data class DistanceQuestDto(
    val commonQuestDto: QuestDto,
    val distance: Double,
    val fullReviewDto: FullReviewsDto
)