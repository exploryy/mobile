package com.example.explory.presentation.screen.map.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.explory.R
import com.example.explory.data.websocket.EventDto
import com.example.explory.data.websocket.EventType
import com.example.explory.domain.model.BuffDto
import com.example.explory.ui.theme.ExploryTheme
import com.example.explory.ui.theme.Gray
import com.example.explory.ui.theme.S20_W600
import com.example.explory.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateLevelContent(event: EventDto, onBuffChoose: (Long) -> Unit) {
    val info = event.text.split(";")
    val level = info[0].toInt()
    val buffs = listOf(
        BuffDto(
            id = 1,
            title = "Увеличение опыта",
            animation = R.raw.exp
        ),
        BuffDto(
            id = 2,
            title = "Увеличение монет",
            animation = R.raw.coins
        )
    )
    Column(
        Modifier
            .size(DIALOG_WIDTH.dp, 350.dp)
            .background(
                BottomSheetDefaults.ContainerColor, shape = RoundedCornerShape(DIALOG_SHAPE.dp)
            )
            .clip(RoundedCornerShape(DIALOG_SHAPE.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Вы достигли %d уровня!".format(level),
            style = S20_W600,
            textAlign = TextAlign.Center,
            color = White
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Выберите улучшение", color = Gray)
        Spacer(modifier = Modifier.weight(1f))
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            buffs.forEach {
                BuffButton(
                    modifier = Modifier.weight(1f),
                    buff = it,
                    onClick = { onBuffChoose(it.id) })
            }
        }

    }
}

@Preview
@Composable
private fun PreviewLevelEvent() {
    ExploryTheme {
        UpdateLevelContent(
            event = EventDto(
                type = EventType.UPDATE_LEVEL,
                text = "2;1"
            ),
            onBuffChoose = {}
        )
    }
}