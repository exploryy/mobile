package com.example.explory.data.model

data class BalanceDto(
    val balance: Int,
    val level: Int,
    val experience: Int? = null,
    val totalExperienceInLevel: Int? = null
)
