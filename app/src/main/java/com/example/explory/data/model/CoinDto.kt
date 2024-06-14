package com.example.explory.data.model

import java.util.UUID

data class CoinDto(
    val coinId: Long,
    val clientId: UUID,
    val latitude: String,
    val longitude: String,
    val taken: Boolean,
    val value: Long
)