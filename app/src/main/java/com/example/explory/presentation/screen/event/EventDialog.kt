package com.example.explory.presentation.screen.event

import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.explory.data.model.event.EventDto
import com.example.explory.data.model.event.EventType
import com.example.explory.presentation.screen.review.SendReviewDialog
import org.koin.androidx.compose.koinViewModel

const val DIALOG_WIDTH = 400
const val DIALOG_HEIGHT = 500
const val DIALOG_SHAPE = 16

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDialog(
    viewModel: EventViewModel = koinViewModel(),
    event: EventDto,
    onDismissRequest: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val eventId = remember { mutableStateOf(null as Long?) }
    BasicAlertDialog(
        onDismissRequest = {
            if (event.type != EventType.UPDATE_LEVEL) {
                onDismissRequest()
            }
        },
    ) {
        when (event.type) {
            EventType.COMPLETE_QUEST -> {
                CompleteQuestContent(event = event,
                    onSendReview = { eventId.value = it })

            }

            EventType.REQUEST_TO_FRIEND -> {
                viewModel.getUserInfo(event.text)
                RequestToFriendContent(user = state.user, onAcceptClick = {
                    viewModel.acceptFriendRequest(it)
                    onDismissRequest()
                }, onDeclineClick = {
                    viewModel.declineFriendRequest(it)
                    onDismissRequest()
                })
            }

            EventType.CHANGE_MONEY -> {}
            EventType.NEW_QUEST -> {}
            EventType.UPDATE_LEVEL -> {
                LaunchedEffect(Unit) {
                    viewModel.getBuffList(level = event.text.split(";")[0].toInt())
                }
                UpdateLevelContent(
                    event,
                    buffs = state.buffs,
                    onDismiss = onDismissRequest,
                    onBuffChoose = {
                        viewModel.applyBuff(it)
                        onDismissRequest()
                    })
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
