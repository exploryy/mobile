package com.example.explory.presentation.screen.map.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.explory.R
import com.example.explory.ui.theme.Black
import com.example.explory.ui.theme.Gray
import com.example.explory.ui.theme.S16_W400
import com.example.explory.ui.theme.S16_W600
import com.example.explory.ui.theme.S20_W600
import com.example.explory.ui.theme.White

@Composable
fun ErrorScreen(modifier: Modifier = Modifier, message: String, onReconnectClicked: () -> Unit) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.error))
    val progress by animateLottieCompositionAsState(
        composition, iterations = LottieConstants.IterateForever
    )
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Black)
            .padding(top = 64.dp, start = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LottieAnimation(
            composition = composition,
            modifier = Modifier.size(200.dp),
            progress = { progress },
        )
        Spacer(modifier = Modifier.size(32.dp))
        Text(text = "Произошла ошибка", style = S20_W600, color = White)
        Spacer(modifier = Modifier.size(16.dp))
        Text(text = message, style = S16_W400, color = Gray, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onReconnectClicked,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = Black,
                containerColor = White
            )
        ) {
            Text(text = "Переподключиться", style = S16_W600)
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview
@Composable
private fun PreviewErrorScreen() {
    ErrorScreen(message = "Нет подключения к сети", onReconnectClicked = {})
}