package com.example.explory.presentation.screen.profile.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.explory.ui.theme.S14_W600

@Composable
fun ProfileButton(
    modifier: Modifier = Modifier, onClick: () -> Unit, text: String, contentColor: Color
) {
    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = contentColor,
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = contentColor
        ),
        modifier = modifier.width(175.dp),
    ) {
        Text(text = text, style = S14_W600)
    }
}