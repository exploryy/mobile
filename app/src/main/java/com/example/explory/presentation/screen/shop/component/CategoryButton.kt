package com.example.explory.presentation.screen.shop.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.explory.presentation.screen.common.getTranslateCategoryName
import com.example.explory.ui.theme.S14_W600
import com.example.explory.ui.theme.Transparent

@Composable
fun CategoryButton(category: String, isSelected: Boolean, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        colors = ButtonDefaults.textButtonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Transparent,
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(horizontal = 4.dp)
    ) {
        Text(
            text = getTranslateCategoryName(category),
            style = S14_W600,
            color = if (isSelected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurface
        )
    }
}