package com.example.explory.presentation.screen.quest

import com.example.explory.data.service.DistanceQuestDto
import com.example.explory.data.service.PointToPointQuestDto

data class QuestState(
    val distanceQuest: DistanceQuestDto? = null,
    val pointToPointQuest: PointToPointQuestDto? = null,
    val isLoading: Boolean = false
)
