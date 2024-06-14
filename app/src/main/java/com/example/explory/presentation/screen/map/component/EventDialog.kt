package com.example.explory.presentation.screen.map.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.explory.data.websocket.EventDto
import com.example.explory.data.websocket.EventType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDialog(event: EventDto, onDismissRequest: () -> Unit) {
    BasicAlertDialog(onDismissRequest = onDismissRequest) {
        Column(
            Modifier
                .size(700.dp, 300.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            Text(text = "Привет, это ивент, видимо что-то произошло...")
            Text(text = "Тип: ${event.type}")
            Text(text = "Сообщение: ${event.text}")
        }
    }
}

@Preview
@Composable
private fun EventDialogPreview() {
    EventDialog(
        event = EventDto(
            type = EventType.COMPLETE_QUEST,
            text = "Текст ивента"
        ),
        onDismissRequest = {}
    )
}