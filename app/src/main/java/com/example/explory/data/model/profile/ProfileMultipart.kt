package com.example.explory.data.model.profile

import android.net.Uri

data class ProfileMultipart(
    val username: String?,
    val email: String?,
    val password: String?,
    val avatar: Uri?
)