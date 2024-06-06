package com.example.explory.presentation.screen.map.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun TopInfoColumn(
    modifier: Modifier = Modifier,
    currentLocationName: String = "Локация",
    currentLocationPercent: Double = 0.0,
) {
    Column(modifier = modifier) {
        Text(
            text = currentLocationName,
            color = Color.White,
        )
        Text(
            text = "$currentLocationPercent%",
            color = Color.White,
        )

    }
}