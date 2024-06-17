package com.example.explory.presentation.screen.auth.onboarding

import androidx.annotation.RawRes
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.explory.R
import com.example.explory.presentation.screen.auth.onboarding.component.PageIndicator
import com.example.explory.ui.theme.Black
import com.example.explory.ui.theme.Gray
import com.example.explory.ui.theme.S16_W400
import com.example.explory.ui.theme.S16_W600
import com.example.explory.ui.theme.S24_W600
import com.example.explory.ui.theme.Value

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.OnBoardingScreen(
    animatedVisibilityScope: AnimatedVisibilityScope, onClickNavigation: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val spacerHeight = 300.dp
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 4 })


    Box(modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures(onTap = {
                focusManager.clearFocus()
            })
        }) {
        Image(
            painter = painterResource(id = R.drawable.earth),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        TopAppBar(title = { Text(text = "") }, navigationIcon = {

        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(
                modifier = Modifier.height(spacerHeight)
            )

            Column(modifier = Modifier
                .fillMaxSize()
                .sharedElement(state = rememberSharedContentState(key = "column"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    boundsTransform = { _, _ ->
                        tween(durationMillis = 500)
                    }), verticalArrangement = Arrangement.Bottom
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                        .background(Color.Black)
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                    ) {
                        HorizontalPager(
                            state = pagerState, modifier = Modifier.weight(1f)
                        ) { page ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                            ) {
                                when (page) {
                                    0 -> PageContent(
                                        title = "Исследуйте мир",
                                        description = "Откройте для себя новые места и путешествуйте по всему миру",
                                        animation = R.raw.explore
                                    )

                                    1 -> PageContent(
                                        title = "Выполняйте квесты",
                                        description = "Выполняйте интересные квесты и получайте награды",
                                        animation = R.raw.quest
                                    )

                                    2 -> PageContent(
                                        title = "Соревнуйтесь с друзьями",
                                        description = "Соревнуйтесь с друзьями и станьте лучшим",
                                        animation = R.raw.friend
                                    )

                                    3 -> PageContent(
                                        title = "Работает в фоне",
                                        description = "Приложение работает в фоне и не требует постоянного включения",
                                        animation = R.raw.foreground
                                    )
                                }
                            }
                        }

                        PageIndicator(
                            pagerState = pagerState
                        )

                        Button(
                            onClick = {
                                onClickNavigation()
                            },
                            shape = RoundedCornerShape(Value.BigRound),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min)
                                .padding(bottom = 16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White
                            ),
                        ) {
                            Text(
                                text = "Поехали!", style = S16_W600, color = Black
                            )
                        }
                    }
                }
            }
        }
    }
}

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
        Text(
            text = title,
            style = S24_W600,
            textAlign = TextAlign.Center,
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