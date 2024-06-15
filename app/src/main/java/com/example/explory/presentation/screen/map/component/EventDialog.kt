package com.example.explory.presentation.screen.map.component

import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.example.explory.data.websocket.EventDto
import com.example.explory.data.websocket.EventType

const val DIALOG_WIDTH = 400
const val DIALOG_HEIGHT = 500
const val DIALOG_SHAPE = 16

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDialog(
    event: EventDto,
    onDismissRequest: () -> Unit,
    onFriendAccept: (String) -> Unit,
    onFriendDecline: (String) -> Unit,
) {
    BasicAlertDialog(onDismissRequest = onDismissRequest) {
        when (event.type) {
            EventType.COMPLETE_QUEST -> {
                CompleteQuestContent(event = event, onDismiss = onDismissRequest)

            }

            EventType.REQUEST_TO_FRIEND -> {
                RequestToFriendContent(
                    event = event,
                    onAcceptClick = {
                        onFriendAccept(it)
                        onDismissRequest()
                    },
                    onDeclineClick = {
                        onFriendDecline(it)
                        onDismissRequest()
                    }
                )
            }

            EventType.CHANGE_MONEY -> {}
        }
    }
}

