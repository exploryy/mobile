package com.example.explory.presentation.screen.quest

//@Composable
//fun QuestScreen(
//    questId: String, questType: String, onNavigateBack: () -> Unit,
//    viewModel: QuestViewModel = koinViewModel()
//) {
//    val state by viewModel.state.collectAsStateWithLifecycle()
//
//    LaunchedEffect(Unit) {
//        viewModel.loadQuest(questId, questType)
//    }
//    when {
//        state.isLoading -> {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Black),
//                contentAlignment = androidx.compose.ui.Alignment.Center
//            ) {
//                CircularProgressIndicator(
//                    color = White,
//                    trackColor = White.copy(alpha = 0.5f),
//                )
//            }
//        }
//
//        else -> {
//            when (questType) {
//                "DISTANCE" -> {
//                    val distanceQuest = state.distanceQuest
//                    if (distanceQuest != null) {
//                        DistanceQuestScreen(
//                            name = distanceQuest.commonQuestDto.name,
//                            description = distanceQuest.commonQuestDto.description,
//                            difficulty = distanceQuest.commonQuestDto.difficultyType,
//                            transportType = distanceQuest.commonQuestDto.transportType,
//                            distance = distanceQuest.distance,
//                            image = distanceQuest.commonQuestDto.images.firstOrNull(),
//                            onNavigateBack = onNavigateBack
//                        )
//                    }
//                }
//
//                "POINT_TO_POINT" -> {
//                    val pointToPointQuest = state.pointToPointQuest
//                    if (pointToPointQuest != null) {
//                        PointToPointQuestScreen(
//                            name = pointToPointQuest.commonQuestDto.name,
//                            description = pointToPointQuest.commonQuestDto.description,
//                            difficulty = pointToPointQuest.commonQuestDto.difficultyType,
//                            transportType = pointToPointQuest.commonQuestDto.transportType,
//                            distance = pointToPointQuest.route.distance,
//                            image = pointToPointQuest.commonQuestDto.images.firstOrNull(),
//                            points = pointToPointQuest.route.points,
//                            onBackNavigate = onNavigateBack
//                        )
//                    }
//                }
//            }
//        }
//    }
//}