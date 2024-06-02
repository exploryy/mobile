package com.example.explory.data.model.profile

import java.io.File

data class ProfileMultipart(
    val username: String?,
    val email: String?,
    val password: String?,
    val avatar: File?
)