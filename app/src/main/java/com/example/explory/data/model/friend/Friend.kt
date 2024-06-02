package com.example.explory.data.model.friend

data class Friend(
    val name: String,
    val status: String,
    val avatarRes: Int,
    val isBestFriend: Boolean = false
)
