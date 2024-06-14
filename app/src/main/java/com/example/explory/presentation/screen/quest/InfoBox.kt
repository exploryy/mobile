package com.example.explory.presentation.screen.quest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.explory.ui.theme.MediumGray
import com.example.explory.ui.theme.S14_W600
import com.example.explory.ui.theme.White

@Composable
fun InfoBox(
    modifier: Modifier = Modifier,
    text: String,
    containerColor: Color = MediumGray,
    textColor: Color = White
) {
    Box(
        modifier = modifier
            .background(containerColor, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
        Text(
            text = text,
            style = S14_W600,
            color = textColor
        )
    }
}