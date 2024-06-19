package com.example.explory.presentation.screen.map.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.explory.R
import com.example.explory.ui.theme.Black
import com.example.explory.ui.theme.Gray
import com.example.explory.ui.theme.P_S18_W500
import com.example.explory.ui.theme.White

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.compass))
    val progress by animateLottieCompositionAsState(
        composition, iterations = LottieConstants.IterateForever
    )
//    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//        LottieAnimation(
//            composition = composition,
//            modifier = Modifier.size(200.dp),
//            progress = { progress },
//        )
//    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black), contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(224.dp))
            Box(
                modifier = Modifier
                    .background(White, shape = RoundedCornerShape(36.dp))
                    .size(120.dp)
                    .clip(shape = RoundedCornerShape(36.dp)),
                contentAlignment = Alignment.Center
            ) {
                LottieAnimation(
                    composition = composition,
                    modifier = Modifier.size(100.dp),
                    progress = { progress },
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "explore the world", style = P_S18_W500, color = Gray)
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}