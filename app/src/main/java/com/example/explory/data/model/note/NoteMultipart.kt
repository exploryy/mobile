package com.example.explory.data.model.note

import android.net.Uri

data class NoteMultipart(
    val text: String,
    val latitude: String,
    val longitude: String,
    val images: List<Uri>
)
