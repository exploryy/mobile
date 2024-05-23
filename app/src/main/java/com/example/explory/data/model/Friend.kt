package com.example.explory.data.model

data class Friend(
    val name: String,
    val status: String,
    val avatarRes: Int,
    val isBestFriend: Boolean = false
)
