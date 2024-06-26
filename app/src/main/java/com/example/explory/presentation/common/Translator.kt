package com.example.explory.presentation.common

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.explory.data.model.shop.RarityType
import com.example.explory.ui.theme.Blue
import com.example.explory.ui.theme.Gray
import com.example.explory.ui.theme.Purple
import com.example.explory.ui.theme.Yellow

@Composable
fun getRarityColor(rarityType: RarityType): Color {
    return when (rarityType) {
        RarityType.COMMON -> Gray
        RarityType.RARE -> Blue
        RarityType.EPIC -> Purple
        RarityType.LEGENDARY -> Yellow
    }
}

@Composable
fun getTranslateCategoryName(name: String): String {
    return when (name) {
        "Все" -> "Все"
        "FOOTPRINT" -> "Следы"
        "AVATAR_FRAMES" -> "Рамки"
        "APPLICATION_IMAGE" -> "Иконки"
        "FOG" -> "Дым"
        else -> "Неизвестно"
    }
}

@Composable
fun getTranslateRareName(name: RarityType): String {
    return when (name) {
        RarityType.COMMON -> "обычный"
        RarityType.EPIC -> "эпический"
        RarityType.LEGENDARY -> "легендарный"
        RarityType.RARE -> "редкий"
    }
}

@Composable
fun getRarityBrush(rarityType: RarityType): Brush {
    val infiniteTransition = rememberInfiniteTransition(label = "")

    val colors = when (rarityType) {
        RarityType.COMMON -> listOf(
            Gray.copy(alpha = 0.8f),
            Gray.copy(alpha = 0.8f)
        )

        RarityType.RARE -> listOf(
            Blue.copy(alpha = 0.8f),
            Blue.copy(alpha = 0.4f)
        )

        RarityType.EPIC -> listOf(
            Purple.copy(alpha = 0.7f),
            Purple.copy(alpha = 0.3f)
        )

        RarityType.LEGENDARY -> listOf(
            Yellow.copy(alpha = 1f),
            Yellow.copy(alpha = 0.3f)
        )
    }

    val animatedColors = infiniteTransition.animateColor(
        initialValue = colors[0],
        targetValue = colors[1],
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    return Brush.linearGradient(
        colors = listOf(animatedColors.value, animatedColors.value.copy(alpha = 0.5f)),
        start = Offset(0f, 1f),
        end = Offset(0f, 0f)
    )
}

fun getDistanceString(distance: Int): String {
    return when {
        distance < 1000 -> "$distance метров"
        distance < 1000000 -> "${distance / 1000} км"
        else -> "${distance / 1000000} тыс. км"
    }
}
