package com.example.explory.presentation.screen.userstatistic

import com.example.explory.data.model.statistic.UserStatisticDto

data class StatisticState(
    val userStatisticDto: UserStatisticDto? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)