package com.example.explory.presentation.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SemiRoundedButtonsRow(
    onFirstButtonClick: () -> Unit,
    onSecondButtonClick: () -> Unit,
    onThirdButtonClick: () -> Unit,
    selectedButton: Int,
    notificationCount: Int
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
            shape = RoundedCornerShape(
                topStart = 16.dp,
                bottomStart = 16.dp,
                topEnd = 8.dp,
                bottomEnd = 8.dp
            ),
            modifier = Modifier
                .height(50.dp)
        ) {
            Text("Друзья")
        }

        Button(
            onClick = onSecondButtonClick,
            colors = if (selectedButton == 2) selectedButtonColor else buttonBaseColor,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .height(50.dp)
        ) {
            Text("Статистика")
        }

        Box {
            Button(
                onClick = onThirdButtonClick,
                colors = if (selectedButton == 3) selectedButtonColor else buttonBaseColor,
                shape = RoundedCornerShape(
                    topStart = 8.dp,
                    bottomStart = 8.dp,
                    topEnd = 16.dp,
                    bottomEnd = 16.dp
                ),
                modifier = Modifier
                    .height(50.dp)
            ) {
                Text("Заявки")
            }

            if (notificationCount > 0) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(Color.Red, CircleShape)
                        .align(Alignment.TopEnd)
                ) {
                    Text(
                        text = notificationCount.toString(),
                        color = Color.White,
                        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}