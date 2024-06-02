package com.example.explory.data.model.friend

data class Friend(
    val name: String,
    val email: String,
    val avatar: String?,
    val isBestFriend: Boolean = false
)
