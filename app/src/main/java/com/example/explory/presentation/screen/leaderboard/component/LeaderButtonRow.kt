package com.example.explory.presentation.screen.leaderboard.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.explory.presentation.screen.profile.component.ProfileTabButton

@Composable
fun LeaderButtonRow(
    onFirstButtonClick: () -> Unit,
    onSecondButtonClick: () -> Unit,
    selectedButton: Int,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ProfileTabButton(
            onClick = onFirstButtonClick,
            text = "пройдено",
            isSelected = selectedButton == 1,
            modifier = Modifier
                .height(40.dp)
                .weight(1f),
        )
        ProfileTabButton(
            onClick = onSecondButtonClick,
            text = "уровень",
            isSelected = selectedButton == 2,
            modifier = Modifier
                .height(40.dp)
                .weight(1f),
        )
    }
}