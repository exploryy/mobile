package com.example.explory.data.model.location

import com.example.explory.data.model.profile.ProfileDto

data class LocationStatisticDto(
    val profileDto: ProfileDto,
    val previousLatitude: String,
    val previousLongitude: String,
    val experience: String,
    val distance: String,
    val level: String,
)


