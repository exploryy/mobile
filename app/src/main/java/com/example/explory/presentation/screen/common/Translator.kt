package com.example.explory.presentation.screen.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.explory.data.model.shop.RarityType

@Composable
fun getRarityColor(rarityType: RarityType): Color {
    return when (rarityType) {
        RarityType.COMMON -> Color.LightGray.copy(alpha = 0.8f)
        RarityType.RARE -> Color.Green.copy(alpha = 0.8f)
        RarityType.EPIC -> Color.Magenta.copy(alpha = 0.8f)
        RarityType.LEGENDARY -> Color.Yellow.copy(alpha = 0.8f)
    }
}

@Composable
fun getTranslateCategoryName(name: String): String {
    return when (name) {
        "Все" -> "все"
        "FOOTPRINT" -> "следы"
        "AVATAR_FRAMES" -> "рамки аватара"
        "APPLICATION_IMAGE" -> "иконки приложения"
        "FOG" -> "дым"
        else -> "Unknown"
    }
}

@Composable
fun getTranslateRareName(name: RarityType): String {
    return when (name) {
        RarityType.COMMON -> "Обычный"
        RarityType.EPIC -> "Эпический"
        RarityType.LEGENDARY -> "Легендарный"
        RarityType.RARE -> "Редкий"
    }
}