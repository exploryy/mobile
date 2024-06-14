package com.example.explory.data.model.profile

data class FriendProfileDto(
    val userId: String,
    val username: String,
    val email: String,
    val photoUrl: String?,
    val previousLatitude: String,
    val previousLongitude: String,
    val level: Int,
    val experience: Int,
    val distance: Int,
    val totalExperienceInLevel: Int
)
