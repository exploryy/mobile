package com.example.explory.data.model.quest

import com.example.explory.data.model.profile.ProfileDto

data class ReviewDto(
    val profile: ProfileDto,
    val questId: Long,
    val createdAt: String,
    val score: Int,
    val message: String,
    val reviewPhotos: List<String>
)