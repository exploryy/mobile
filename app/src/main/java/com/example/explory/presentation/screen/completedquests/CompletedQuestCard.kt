package com.example.explory.presentation.screen.completedquests

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.explory.data.model.quest.CompletedQuestDto
import com.example.explory.presentation.common.Avatar
import com.example.explory.presentation.screen.quest.component.InfoBox
import com.example.explory.ui.theme.S14_W600

@Composable
fun CompletedQuestCard(
    quest: CompletedQuestDto,
    onClick: () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Avatar(
                image = if (quest.images.isNotEmpty()) quest.images.first() else null,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = quest.name,
                style = S14_W600,
                color = MaterialTheme.colorScheme.onBackground,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        InfoBox(text = getCorrectType(quest.questType))
    }
}

fun getCorrectType(type: String): String {
    return when (type) {
        "POINT_TO_POINT" -> "добраться до точки"
        "DISTANCE" -> "пройти расстояние"
        else -> "неизвестный"
    }
}