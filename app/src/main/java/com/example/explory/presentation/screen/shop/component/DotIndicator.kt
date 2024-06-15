package com.example.explory.presentation.screen.shop.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.explory.R
import com.example.explory.ui.theme.Value.CenterPadding

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DotIndicator(
    state: PagerState,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .padding(bottom = CenterPadding)
            .clip(RoundedCornerShape(28.dp))
            .background(color = Color.Black.copy(alpha = 0.25f))
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            repeat(3) { index ->
                Box(
                    modifier = Modifier
                        .size(8.dp)
                ){
                    if(state.currentPage == index) {
                        Icon(
                            painterResource(R.drawable.dot_focused),
                            contentDescription = null
                        )
                    } else {
                        Icon(
                            painterResource(R.drawable.dot),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}