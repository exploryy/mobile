package com.example.explory.presentation.screen.leaderboard

import com.example.explory.data.model.leaderboard.TotalStatisticDto

data class LeaderboardState(
    val leadersCount: Int = 0,
    val leadersList: TotalStatisticDto? = null,
    val currentScreen: Int = 1
)
