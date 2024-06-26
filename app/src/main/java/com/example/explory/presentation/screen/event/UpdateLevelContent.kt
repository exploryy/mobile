package com.example.explory.presentation.screen.event

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.explory.R
import com.example.explory.data.model.event.EventDto
import com.example.explory.data.model.event.EventType
import com.example.explory.domain.model.BuffDto
import com.example.explory.ui.theme.Black
import com.example.explory.ui.theme.ExploryTheme
import com.example.explory.ui.theme.S20_W600
import com.example.explory.ui.theme.White

@Composable
fun UpdateLevelContent(
    event: EventDto,
    onDismiss: () -> Unit,
    onBuffChoose: (Long) -> Unit,
    buffs: List<BuffDto>,
) {
    val info = event.text.split(";")
    val level = info[0].toInt()
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier
                .size(DIALOG_WIDTH.dp, 350.dp)
                .background(
                    MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(DIALOG_SHAPE.dp)
                )
                .clip(RoundedCornerShape(DIALOG_SHAPE.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Вы достигли %d уровня!".format(level),
                style = S20_W600,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (buffs.isEmpty()) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "На данный момент улучшений нет",
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.weight(1f))

            } else {
                Text(text = "Выберите улучшение", color = MaterialTheme.colorScheme.onSurface)
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
        if (buffs.isEmpty()) {
            IconButton(colors = IconButtonDefaults.iconButtonColors(
                contentColor = Black, containerColor = White
            ),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .size(24.dp)
                    .clip(CircleShape),
                onClick = {
                    onDismiss()
                }) {
                Icon(
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = null,
                    tint = Black,
                    modifier = Modifier.scale(0.8f)
                )
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
            onBuffChoose = {},
            onDismiss = {},
            buffs = emptyList()
        )
    }
}