package com.example.explory.presentation.screen.map.utils

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Paint
import android.graphics.Shader

fun createCircularAvatar(bitmap: Bitmap, targetSize: Int): Bitmap {
    val scaledAvatarBitmap = Bitmap.createScaledBitmap(
        bitmap, targetSize, targetSize, false
    )

    val circleBitmap = Bitmap.createBitmap(
        scaledAvatarBitmap.width, scaledAvatarBitmap.height, Bitmap.Config.ARGB_8888
    )
    val canvas = android.graphics.Canvas(circleBitmap)
    val paint = Paint().apply {
        isAntiAlias = true
        shader = BitmapShader(
            scaledAvatarBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP
        )
    }
    val radius = scaledAvatarBitmap.width / 2f
    canvas.drawCircle(radius, radius, radius, paint)

    return circleBitmap
}