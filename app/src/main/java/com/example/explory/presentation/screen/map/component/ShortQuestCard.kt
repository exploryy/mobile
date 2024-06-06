package com.example.explory.presentation.screen.map.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.explory.R

@Composable
fun ShortQuestCard(questType: String, difficultyColor: Color, name: String) {
    Box(
        modifier = Modifier
            .background(Color.White)
    ) {
        Column(modifier = Modifier.width(200.dp)) {
            Row {
                Text(name)
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(16.dp, 16.dp)
                        .clip(CircleShape)
                        .background(difficultyColor)
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = R.drawable.picture),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(questType)
            Button(
                onClick = { /*TODO*/ }, modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Подробнее")
            }
        }
    }
}

@Preview
@Composable
private fun ShortCardPreview() {
    ShortQuestCard("Добраться до точки", Color.Red, "Квест 1")
}