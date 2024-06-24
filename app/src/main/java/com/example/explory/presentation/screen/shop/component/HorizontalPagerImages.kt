package com.example.explory.presentation.screen.shop.component

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.explory.R
import com.example.explory.ui.theme.S20_W600
import com.example.explory.ui.theme.S24_W600
import kotlinx.coroutines.delay

@Composable
fun HorizontalPagerImages() {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val pagerHeight = screenHeight * 0.45f

    val pagerState = rememberPagerState(pageCount = { 2 })

    LaunchedEffect(Unit) {
        while (true) {
            delay(7000)
            pagerState.animateScrollToPage(
                page = (pagerState.currentPage + 1) % pagerState.pageCount,
                animationSpec = tween(durationMillis = 500)
            )
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(Color.Transparent)
                .fillMaxWidth()
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(pagerHeight)
            ) { page ->
                when (page) {
                    0 -> Banner()
                    1 -> Banner2()
//                    2 -> Banner()
                }
//                Image(
//                    painter = painterResource(id = getPageImage(page)),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .clip(RoundedCornerShape(8.dp)),
//                    contentScale = ContentScale.Crop
//                )
            }
            DotIndicator(
                state = pagerState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun Banner() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.surfaceVariant
            ), contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "сезон начат",
                style = S24_W600
            )
//            Spacer(modifier = Modifier.height(16.dp))
//            Text(
//                text = "explore the world",
//                style = P_S18_W500,
//                color = MaterialTheme.colorScheme.onSurface
//            )
        }
    }
}

@Composable
fun Banner2() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(id = R.drawable.infogr1),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
//                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Text(
            text = "как далеко вы сможете отдалиться от точки за час?",
            textAlign = TextAlign.Center,
            style = S20_W600,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}

fun getPageImage(page: Int): Int {
    return when (page) {
        0 -> R.drawable.shop1
        1 -> R.drawable.shop2
        2 -> R.drawable.shop3
        else -> R.drawable.infogr1
    }
}