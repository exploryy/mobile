package com.example.explory.data.model.quest

data class PointToPointQuestDto(
    val commonQuestDto: QuestDto,
    val route: RouteDto,
    val questReviews: FullReviewsDto
)