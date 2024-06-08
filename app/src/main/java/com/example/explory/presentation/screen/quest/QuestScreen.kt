package com.example.explory.presentation.screen.quest

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun QuestScreen(
    questId: String, questType: String, onNavigateBack: () -> Unit,
    viewModel: QuestViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadQuest(questId, questType)
    }

    when (questType) {
        "DISTANCE" -> {
            val distanceQuest = state.distanceQuest
            if (distanceQuest != null) {
                DistanceQuestScreen(
                    name = distanceQuest.commonQuestDto.name,
                    description = distanceQuest.commonQuestDto.description,
                    difficulty = distanceQuest.commonQuestDto.difficultyType,
                    transportType = distanceQuest.commonQuestDto.transportType,
                    distance = distanceQuest.distance,
                    image = distanceQuest.commonQuestDto.images.firstOrNull(),
                    onNavigateBack = onNavigateBack
                )
            }
        }

        "POINT_TO_POINT" -> {
            val pointToPointQuest = state.pointToPointQuest
            if (pointToPointQuest != null) {
                PointToPointQuestScreen(
                    name = pointToPointQuest.commonQuestDto.name,
                    description = pointToPointQuest.commonQuestDto.description,
                    difficulty = pointToPointQuest.commonQuestDto.difficultyType,
                    transportType = pointToPointQuest.commonQuestDto.transportType,
                    distance = pointToPointQuest.route.distance,
                    image = pointToPointQuest.commonQuestDto.images.firstOrNull(),
                    points = pointToPointQuest.route.points,
                    onBackNavigate = onNavigateBack
                )
            }
        }

    }
}