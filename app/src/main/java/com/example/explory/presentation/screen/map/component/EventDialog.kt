package com.example.explory.presentation.screen.map.component

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.explory.R
import com.example.explory.data.websocket.EventDto
import com.example.explory.data.websocket.EventType
import com.example.explory.ui.theme.Black
import com.example.explory.ui.theme.DarkGray
import com.example.explory.ui.theme.MediumGray
import com.example.explory.ui.theme.S16_W600
import com.example.explory.ui.theme.S20_W600
import com.example.explory.ui.theme.S24_W600
import com.example.explory.ui.theme.White

const val DIALOG_WIDTH = 400
const val DIALOG_HEIGHT = 500
const val DIALOG_SHAPE = 16

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDialog(event: EventDto, onDismissRequest: () -> Unit) {
    BasicAlertDialog(onDismissRequest = onDismissRequest) {
        when (event.type) {
            EventType.COMPLETE_QUEST -> {
                CompleteQuestContent(event = event)

            }

            EventType.REQUEST_TO_FRIEND -> {
                RequestToFriendContent(event = event)
            }

            EventType.CHANGE_MONEY -> {}
        }


    }
}

@Composable
private fun CompleteQuestContent(event: EventDto) {
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
            onClick = { /*TODO*/ },
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

@Composable
fun RewardBox(modifier: Modifier = Modifier, count: Int, icon: Int) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(16.dp))
            .background(White.copy(0.3f), shape = RoundedCornerShape(16.dp))
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.width(60.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
            Text(
                text = count.toString(),
                style = S16_W600
            )
        }
    }
}

@Composable
private fun RequestToFriendContent(event: EventDto) {
    Column(
        Modifier
            .width(DIALOG_WIDTH.dp)
            .background(White, shape = RoundedCornerShape(DIALOG_SHAPE.dp))
            .clip(RoundedCornerShape(DIALOG_SHAPE.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = event.text,
            style = S24_W600,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(64.dp))
        Avatar(image = null, modifier = Modifier.size(75.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Имя Фамилия",
            style = S20_W600
        )
        Spacer(modifier = Modifier.height(64.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { /*TODO*/ },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = White,
                    containerColor = Black
                )
            ) {
                Text(text = "Отклонить", style = S16_W600)
            }
            Button(
                onClick = { /*TODO*/ },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Black,
                    containerColor = White
                )
            ) {
                Text(text = "Принять", style = S16_W600)
            }
        }
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
            onDismissRequest = {}
        )
    }
}

@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    image: String?,
) {
    val ctx = LocalContext.current
    val imageLoader = ImageLoader.Builder(ctx).build()
    Box(
        modifier = modifier.background(color = DarkGray, shape = CircleShape)
    ) {
        SubcomposeAsyncImage(
            model = image,
            imageLoader = imageLoader,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.Center)
                .clip(CircleShape)
                .fillMaxSize(),
            alignment = Alignment.Center
        ) {
            when (painter.state) {
                is AsyncImagePainter.State.Success -> {
                    SubcomposeAsyncImageContent()
                }

                is AsyncImagePainter.State.Error -> {
                    Icon(
                        painter = painterResource(id = R.drawable.picture),
                        contentDescription = null,
                        tint = White,
                        modifier = Modifier.scale(0.5f)
                    )
                }

                is AsyncImagePainter.State.Empty -> {
                    Icon(
                        painter = painterResource(id = R.drawable.picture),
                        contentDescription = null,
                        tint = White,
                        modifier = Modifier.scale(0.5f)
                    )
                }

                is AsyncImagePainter.State.Loading -> {
                    CircularProgressIndicator(
                        color = White,
                        trackColor = White.copy(alpha = 0.3f),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewRequestToFriendDialog() {
    EventDialog(
        event = EventDto(
            text = "Новая заявка",
            type = EventType.REQUEST_TO_FRIEND
        ),
        onDismissRequest = {}
    )
}
