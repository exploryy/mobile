package com.example.explory.presentation.screen.common

import androidx.compose.runtime.Composable
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