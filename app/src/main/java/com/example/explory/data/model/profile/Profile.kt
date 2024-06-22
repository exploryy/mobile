package com.example.explory.data.model.profile

data class Profile(
    val id: String,
    val name: String,
    val email: String,
    val avatarUri: String? = null
)

//fun ProfileDto.toProfile(): Profile {
//    return Profile(
//        id = this.userId,
//        name = this.username,
//        email = this.email,
//        avatarUri = this.avatarUrl
//    )
//}
