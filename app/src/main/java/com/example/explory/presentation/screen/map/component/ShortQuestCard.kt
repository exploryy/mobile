package com.example.explory.presentation.screen.map.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.explory.ui.theme.Black
import com.example.explory.ui.theme.S14_W400
import com.example.explory.ui.theme.S14_W600
import com.example.explory.ui.theme.S16_W600

@Composable
fun ShortQuestCard(
    questType: String,
    difficultyColor: Color,
    name: String,
    onDetailsClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .widthIn(max = 200.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White, shape = RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(name, style = S16_W600, color = Black)
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(8.dp, 8.dp)
                        .clip(CircleShape)
                        .background(difficultyColor)
                )

            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(questType, style = S14_W400, color = Black)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                shape = RoundedCornerShape(8.dp),
                onClick = onDetailsClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Подробнее", style = S14_W600, color = Color.White)
            }
        }
    }
}

@Preview
@Composable
private fun ShortCardPreview() {
    ShortQuestCard("Добраться до точки", Color.Red, "Квест 1", onDetailsClick = { })
}