package com.example.explory.presentation.screen.quest

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
fun QuestImage(modifier: Modifier = Modifier, image: String?) {
    AsyncImage(
        model = image ?: "",
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .background(Color.White)
    )
}