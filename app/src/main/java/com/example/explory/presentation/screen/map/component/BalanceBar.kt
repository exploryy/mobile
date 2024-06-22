package com.example.explory.presentation.screen.map.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.explory.R
import com.example.explory.ui.theme.AccentColor
import kotlinx.coroutines.delay

@Composable
fun BalanceBar(
    modifier: Modifier = Modifier, balance: Int, level: Int, exp: Int, expToNextLevel: Int
) {
    var isExpBarVisible by remember { mutableStateOf(false) }

    LaunchedEffect(exp) {
        isExpBarVisible = true
        delay(3000)
        isExpBarVisible = false
    }

    Column(
        modifier = modifier.width(
            when {
                balance < 10 -> 80.dp
                balance < 100 -> 100.dp
                balance > 1000 && level > 10 -> 120.dp
                else -> 110.dp
            }
        ),
        horizontalAlignment = Alignment.End
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BarItem(value = balance, icon = R.drawable.coin)
            Spacer(
                modifier = Modifier
                    .weight(1f)
            )
            BarItem(value = level, icon = R.drawable.star)
        }
        AnimatedVisibility(
            visible = isExpBarVisible,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp)
        ) {
            LinearProgressIndicator(
                progress = { exp.toFloat() / expToNextLevel },
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp)),
                color = AccentColor,
                trackColor = Color.White,
            )
        }
    }
}