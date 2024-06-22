package com.example.explory.data.model.review

import android.net.Uri

data class SendReviewRequest(
    val eventId: Long,
    val rating: Int,
    val reviewText: String?,
    val images: List<Uri>
)