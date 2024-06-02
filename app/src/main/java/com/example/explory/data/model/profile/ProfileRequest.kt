package com.example.explory.data.model.profile

import android.net.Uri

data class ProfileRequest(
    val username: String?,
    val email: String?,
    val password: String?,
    val avatar: Uri?
)