package com.example.explory.data.model.event

data class BuffResponse(
    val buffId: Long,
    val valueFactor: Double,
    val status: BuffType,
    val levelNumber: Int
)