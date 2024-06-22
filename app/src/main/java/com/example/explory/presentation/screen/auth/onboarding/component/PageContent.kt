package com.example.explory.presentation.screen.auth.onboarding.component

import androidx.annotation.RawRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
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
import com.example.explory.ui.theme.Gray
import com.example.explory.ui.theme.S16_W400
import com.example.explory.ui.theme.S24_W600
import com.example.explory.ui.theme.White

@Composable
fun PageContent(
    modifier: Modifier = Modifier, title: String, description: String, @RawRes animation: Int
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(animation))
    val progress by animateLottieCompositionAsState(
        composition, iterations = LottieConstants.IterateForever
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
    ) {
        LottieAnimation(
            composition = composition,
            modifier = Modifier.size(200.dp),
            progress = { progress },
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            style = S24_W600,
            textAlign = TextAlign.Center,
            color = White
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description,
            style = S16_W400,
            color = Gray,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
private fun PreviewPageContent() {
    PageContent(
        title = "Title",
        description = "Some text that should be quite long to see how it looks on the screen.",
        animation = R.raw.success
    )
}