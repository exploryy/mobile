package com.example.explory.presentation.screen.battlepass.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.explory.ui.theme.Green

@Composable
fun CustomBar(progress: Float, backgroundColor: Color) {
    Canvas(
        modifier = Modifier
            .size(10.dp, 125.dp)
            .rotate(if (progress == 0f || progress == 100f) 0f else 180f)
    ) {
        drawRect(
            color = Green,
            size = Size(10.dp.toPx(), (progress * 125).dp.toPx()),
            topLeft = Offset(0.dp.toPx(), ((1 - progress) * 125).dp.toPx())
        )
        drawRect(
            color = backgroundColor,
            size = Size(10.dp.toPx(), 125.dp.toPx()),
            topLeft = Offset(0.dp.toPx(), (progress * 125).dp.toPx())
        )
    }
}