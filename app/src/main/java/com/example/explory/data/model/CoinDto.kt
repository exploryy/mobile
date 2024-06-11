package com.example.explory.data.model

data class CoinDto(
    val coin_id: Long,
    val client_id: Long,
    val latitude: String,
    val longitude: String,
    val taken: Boolean,
    val value: Long
)