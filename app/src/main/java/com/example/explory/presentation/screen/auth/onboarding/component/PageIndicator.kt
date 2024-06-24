package com.example.explory.presentation.screen.auth.onboarding.component

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PageIndicator(
    modifier: Modifier = Modifier,
    pagerState: PagerState
) {
    val pageCount = pagerState.pageCount
    if (pageCount <= 1) return
    Row(
        modifier
            .height(24.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        repeat(pageCount) { iteration ->
            val lineWeight = animateFloatAsState(
                targetValue = if (pagerState.currentPage == iteration) {
                    1.5f
                } else {
                    if (iteration < pagerState.currentPage) {
                        0.5f
                    } else {
                        1f
                    }
                }, label = "size", animationSpec = tween(300, easing = EaseInOut)
            )
            val color =
                if (pagerState.currentPage == iteration) Color.White else Color.White.copy(alpha = 0.5f)
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(color)
                    .weight(lineWeight.value)
                    .height(4.dp)
            )
        }
    }
}