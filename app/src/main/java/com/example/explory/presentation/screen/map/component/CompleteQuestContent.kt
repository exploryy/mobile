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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.explory.R
import com.example.explory.data.model.event.EventDto
import com.example.explory.data.model.event.EventType
import com.example.explory.ui.theme.ExploryTheme
import com.example.explory.ui.theme.S16_W600
import com.example.explory.ui.theme.S20_W600

@Composable
fun CompleteQuestContent(
    event: EventDto,
    onSendReview: (Long) -> Unit
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.success))
    val progress by animateLottieCompositionAsState(
        composition, iterations = LottieConstants.IterateForever
    )
    val info = event.text.split(";")
    val name = info[0].lowercase()
    val exp = info[1].toInt()
    val coins = info[2].toInt()
    val questId = info[3].toLong()
    Column(
        Modifier
            .size(DIALOG_WIDTH.dp, DIALOG_HEIGHT.dp)
            .background(
                MaterialTheme.colorScheme.background, shape = RoundedCornerShape(DIALOG_SHAPE.dp)
            )
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
            text = "Квест «%s» завершён!".format(name),
            style = S20_W600,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Ваши награды", style = S16_W600, color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RewardBox(
                count = coins, icon = R.drawable.coin
            )
            RewardBox(count = exp, icon = R.drawable.exp)
        }
        Spacer(
            modifier = Modifier
                .weight(1f)
                .height(16.dp)
        )
        Button(
            onClick = {
                onSendReview(questId)
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.onPrimary,
                containerColor = MaterialTheme.colorScheme.primary
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
    ExploryTheme {
        CompleteQuestContent(
            event = EventDto(
                text = "1;2;3;1", type = EventType.COMPLETE_QUEST
            )
        ) {
        }
    }
}