package com.example.explory.presentation.screen.map.component

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.explory.R
import com.example.explory.data.websocket.EventDto
import com.example.explory.data.websocket.EventType
import com.example.explory.ui.theme.Black
import com.example.explory.ui.theme.MediumGray
import com.example.explory.ui.theme.S16_W600
import com.example.explory.ui.theme.S24_W600
import com.example.explory.ui.theme.White

@Composable
fun CompleteQuestContent(event: EventDto, onDismiss: () -> Unit) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.success))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )
    Column(
        Modifier
            .size(DIALOG_WIDTH.dp, DIALOG_HEIGHT.dp)
            .background(White, shape = RoundedCornerShape(DIALOG_SHAPE.dp))
            .clip(RoundedCornerShape(DIALOG_SHAPE.dp))
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            composition = composition,
            modifier = Modifier.size(200.dp),
            progress = { progress },
        )
        Text(
            text = event.text,
            style = S24_W600
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Ваши награды",
            style = S16_W600,
            color = MediumGray
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RewardBox(
                count = 100,
                icon = R.drawable.coin
            )
            RewardBox(count = 1337, icon = R.drawable.exp)
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onDismiss,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = Black,
                containerColor = White
            )
        ) {
            Text(text = "Забрать", style = S16_W600)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview
@Composable
private fun PreviewQuestCompletedDialog() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
    ) {
        EventDialog(
            event = EventDto(
                text = "Квест завершён!",
                type = EventType.COMPLETE_QUEST
            ),
            onDismissRequest = {},
            onFriendAccept = {},
            onFriendDecline = {}
        )
    }
}