package com.example.explory.presentation.screen.shop.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CategoryButton(category: String, isSelected: Boolean, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        colors = ButtonDefaults.textButtonColors(
            contentColor = if (isSelected) Color.Gray else Color.DarkGray
        ),
        modifier = Modifier
            .padding(horizontal = 4.dp)
    ) {
        Text(
            text = category,
            color = if (isSelected) Color.White else Color.LightGray
        )
    }
}