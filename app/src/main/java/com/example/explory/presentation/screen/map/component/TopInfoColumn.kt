package com.example.explory.presentation.screen.map.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.explory.ui.theme.S20_W600
import com.example.explory.ui.theme.S24_W600

@Composable
fun TopInfoColumn(
    modifier: Modifier = Modifier,
    currentLocationName: String = "Локация",
    currentLocationPercent: Double = 0.0,
) {
    Column(modifier = modifier) {
        Text(
            text = currentLocationName.lowercase(),
            color = Color.White,
            style = S24_W600,
            textDecoration = TextDecoration.Underline,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "$currentLocationPercent%",
            color = Color.White,
            style = S20_W600,
        )
    }
}