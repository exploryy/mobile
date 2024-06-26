package com.example.explory.presentation.screen.quest

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.explory.data.model.quest.FullReviewsDto
import com.example.explory.data.model.quest.PointDto
import com.example.explory.ui.theme.ExploryTheme

@Preview
@Composable
private fun PreviewQuestSheet() {
    ExploryTheme {
        QuestSheet(
            name = "Длинное название квеста квест квест квест",
            images = emptyList(),
            point = PointDto("0.0", "0.0"),
            description = "Описание",
            difficulty = "Сложность",
            distance = 100.0,
            onButtonClicked = { },
            onDismissRequest = { },
            reviews = FullReviewsDto(
                avg = 4.5,
                reviews = emptyList()
            ),
            isCompleted = false
        )
    }
}