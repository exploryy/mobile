package com.example.explory.presentation.screen.map

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader

fun createCircularAvatar(bitmap: Bitmap, frameBitmap: Bitmap? = null, targetSize: Int, frameScale: Float = 1.1f): Bitmap {
    val scaledAvatarBitmap = Bitmap.createScaledBitmap(
        bitmap, targetSize, targetSize, false
    )

    val circleBitmap = Bitmap.createBitmap(
        (targetSize * frameScale).toInt(), (targetSize * frameScale).toInt(), Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(circleBitmap)
    val paint = Paint().apply {
        isAntiAlias = true
        shader = BitmapShader(
            scaledAvatarBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP
        )
    }
    val radius = targetSize / 2f
    val center = (targetSize * frameScale) / 2f
    canvas.drawCircle(center, center, radius, paint)

    frameBitmap?.let {
        val scaledFrameBitmap = Bitmap.createScaledBitmap(
            it, (targetSize * frameScale).toInt(), (targetSize * frameScale).toInt(), false
        )
        canvas.drawBitmap(scaledFrameBitmap, 0f, 0f, null)
    }

    return circleBitmap
}

