package com.example.explory.data.model.note

import com.example.explory.data.model.profile.ProfileDto

data class NoteDto(
    val id: Long,
    val profile: ProfileDto,
    val latitude: String,
    val longitude: String,
    val note: String,
    val createdAt: String,
    val images: List<String>
)
