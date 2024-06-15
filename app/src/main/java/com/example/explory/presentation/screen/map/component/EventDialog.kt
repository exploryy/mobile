package com.example.explory.presentation.screen.map.component

import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.explory.data.websocket.EventDto
import com.example.explory.data.websocket.EventType
import org.koin.androidx.compose.koinViewModel

const val DIALOG_WIDTH = 400
const val DIALOG_HEIGHT = 500
const val DIALOG_SHAPE = 16

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDialog(
    viewModel: EventViewModel = koinViewModel(),
    event: EventDto,
    onDismissRequest: () -> Unit,
    onFriendAccept: (String) -> Unit,
    onFriendDecline: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    BasicAlertDialog(onDismissRequest = onDismissRequest) {
        when (event.type) {
            EventType.COMPLETE_QUEST -> {
                CompleteQuestContent(event = event, onDismiss = onDismissRequest)

            }

            EventType.REQUEST_TO_FRIEND -> {
                viewModel.getUserInfo(event.text)
                RequestToFriendContent(
                    user = state.user,
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

