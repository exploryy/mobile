package com.example.explory.domain.model

import com.example.explory.data.model.quest.TransportType

data class SelectTransport(
    val title: String,
    val description: String,
    val animation: Int,
    val transportType: TransportType
)