package com.example.explory.presentation.screen.map.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import com.example.explory.R

fun createDefaultAvatar(context: Context, frameBitmap: Bitmap?): Bitmap {
    val defaultAvatarSize = 100
    val defaultAvatarBitmap = Bitmap.createBitmap(
        defaultAvatarSize, defaultAvatarSize, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(defaultAvatarBitmap)
    val paint = Paint().apply {
        isAntiAlias = true
    }

    val radius = defaultAvatarSize / 2f

    paint.color = android.graphics.Color.GRAY
    canvas.drawCircle(radius, radius, radius, paint)

    val defaultPicture = BitmapFactory.decodeResource(context.resources, R.drawable.picture1)

    if (defaultPicture != null) {
        return createCircularAvatar(defaultPicture, frameBitmap, defaultAvatarSize)

    } else {
        paint.color = android.graphics.Color.RED
        paint.textSize = 20f
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText("No Image", radius, radius, paint)
        return defaultAvatarBitmap
    }
}