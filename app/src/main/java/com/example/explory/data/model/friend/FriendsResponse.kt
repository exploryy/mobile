package com.example.explory.data.model.friend

import com.example.explory.data.model.profile.ProfileDto

data class FriendsResponse(
    val friends: List<ProfileDto>,
    val favoriteFriends: List<ProfileDto>
)