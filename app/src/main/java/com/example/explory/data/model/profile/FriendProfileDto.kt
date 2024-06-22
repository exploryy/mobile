package com.example.explory.data.model.profile

data class FriendProfileDto(
    val previousLatitude: String?,
    val previousLongitude: String?,
    val level: Int?,
    val experience: Int?,
    val distance: Int?,
    val totalExperienceInLevel: Int?,
    val profileDto: ProfileDto
)
