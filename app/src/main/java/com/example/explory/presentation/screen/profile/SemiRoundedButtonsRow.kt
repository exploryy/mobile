package com.example.explory.presentation.screen.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SemiRoundedButtonsRow(
    onFirstButtonClick: () -> Unit,
    onSecondButtonClick: () -> Unit,
    onThirdButtonClick: () -> Unit,
    selectedButton: Int
) {
    val buttonBaseColor = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
        disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.4f)
    )

    val selectedButtonColor = ButtonDefaults.buttonColors(
        containerColor = Color.White,
        contentColor = MaterialTheme.colorScheme.primary,
        disabledContainerColor = Color.White.copy(alpha = 0.4f),
        disabledContentColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = onFirstButtonClick,
            colors = if (selectedButton == 1) selectedButtonColor else buttonBaseColor,
            shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp),
            modifier = Modifier
                .weight(1f)
                .height(50.dp)
        ) {
            Text("Друзья")
        }

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = onSecondButtonClick,
            colors = if (selectedButton == 2) selectedButtonColor else buttonBaseColor,
            shape = RoundedCornerShape(0.dp),
            modifier = Modifier
                .weight(1f)
                .height(50.dp)
        ) {
            Text("Статистика")
        }

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = onThirdButtonClick,
            colors = if (selectedButton == 3) selectedButtonColor else buttonBaseColor,
            shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp),
            modifier = Modifier
                .weight(1f)
                .height(50.dp)
        ) {
            Text("Заявки")
        }
    }
}