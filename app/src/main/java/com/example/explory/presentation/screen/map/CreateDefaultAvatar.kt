package com.example.explory.presentation.screen.map

import android.graphics.Bitmap
import android.graphics.Paint

fun createDefaultAvatar(): Bitmap {
    val defaultAvatarSize = 100
    val defaultAvatarBitmap = Bitmap.createBitmap(
        defaultAvatarSize, defaultAvatarSize, Bitmap.Config.ARGB_8888
    )
    val canvas = android.graphics.Canvas(defaultAvatarBitmap)
    val paint = Paint().apply {
        isAntiAlias = true
        color = android.graphics.Color.GRAY
    }
    val radius = defaultAvatarSize / 2f
    canvas.drawCircle(radius, radius, radius, paint)

    return defaultAvatarBitmap
}