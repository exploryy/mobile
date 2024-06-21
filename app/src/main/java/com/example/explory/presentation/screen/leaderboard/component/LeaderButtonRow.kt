package com.example.explory.presentation.screen.leaderboard.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LeaderButtonRow(
    onFirstButtonClick: () -> Unit,
    onSecondButtonClick: () -> Unit,
    selectedButton: Int,
){
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
        horizontalArrangement = Arrangement.spacedBy(16.dp)
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
                .height(40.dp)
                .weight(1f)
        ) {
            Text(
                text = "По дистанции",
                overflow = TextOverflow.Ellipsis,
                fontSize = 15.sp
            )
        }

        Button(
            onClick = onSecondButtonClick,
            colors = if (selectedButton == 2) selectedButtonColor else buttonBaseColor,
            shape = RoundedCornerShape(
                topStart = 8.dp,
                bottomStart = 8.dp,
                topEnd = 16.dp,
                bottomEnd = 16.dp
            ),
            modifier = Modifier
                .height(40.dp)
                .weight(1f)
        ) {
            Text(
                text = "По уровню",
                overflow = TextOverflow.Ellipsis,
                fontSize = 15.sp
            )
        }
    }
}