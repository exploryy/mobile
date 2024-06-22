package com.example.explory.presentation.screen.map.component

import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.explory.data.model.event.EventDto
import com.example.explory.data.model.event.EventType
import com.example.explory.presentation.screen.map.component.review.SendReviewDialog
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
    val eventId = remember { mutableStateOf(null as Long?) }
    BasicAlertDialog(
        onDismissRequest = onDismissRequest
    ) {
        when (event.type) {
            EventType.COMPLETE_QUEST -> {
                CompleteQuestContent(event = event,
                    onSendReview = { eventId.value = it })

            }

            EventType.REQUEST_TO_FRIEND -> {
                viewModel.getUserInfo(event.text)
                RequestToFriendContent(user = state.user, onAcceptClick = {
                    onFriendAccept(it)
                    onDismissRequest()
                }, onDeclineClick = {
                    onFriendDecline(it)
                    onDismissRequest()
                })
            }

            EventType.CHANGE_MONEY -> {}
            EventType.NEW_QUEST -> {}
            EventType.UPDATE_LEVEL -> {
                viewModel.getBuffList()
                UpdateLevelContent(
                    event,
                    buffs = state.buffs,
                    onBuffChoose = { viewModel.applyBuff(it) })
            }

            EventType.UPDATE_EXPERIENCE -> {}
            EventType.UPDATE_BATTLE_PASS_LEVEL -> TODO()
        }
    }
    if (eventId.value != null) {
        SendReviewDialog(onSendReviewClicked = { rating, review, images ->
            viewModel.sendReview(eventId.value!!, rating, review, images)
        }, onDismiss = {
            eventId.value = null
            onDismissRequest()
        })

    }
}
