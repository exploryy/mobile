package com.example.explory.data.model.requests

import com.example.explory.data.model.profile.ProfileDto

data class FriendRequest (
    val my: List<ProfileDto>,
    val other: List<ProfileDto>
)