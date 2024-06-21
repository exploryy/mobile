package com.example.explory.data.model.leaderboard

import com.example.explory.data.model.location.LocationStatisticDto

data class TotalStatisticDto(
    val bestUsers: List<LocationStatisticDto>,
    val userPosition: Int
)
