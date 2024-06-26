package com.example.explory.presentation.screen.profile.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SemiRoundedButtonsRow(
    onFirstButtonClick: () -> Unit,
    onSecondButtonClick: () -> Unit,
    onThirdButtonClick: () -> Unit,
    selectedButton: Int,
    notificationCount: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ProfileTabButton(
            modifier = Modifier.weight(1f),
            onClick = onFirstButtonClick,
            text = "друзья",
            isSelected = selectedButton == 1,
            notification = null
        )
        ProfileTabButton(
            modifier = Modifier.weight(1f),
            onClick = onSecondButtonClick,
            text = "инфо",
            isSelected = selectedButton == 2,
            notification = null
        )
        ProfileTabButton(
            modifier = Modifier.weight(1f),
            onClick = onThirdButtonClick,
            text = "заявки",
            isSelected = selectedButton == 3,
            notification = notificationCount
        )
    }
}

