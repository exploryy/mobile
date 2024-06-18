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
        modifier = modifier
            //            .background(Color.White.copy(0.15f), shape = RoundedCornerShape(8.dp))
//            .clip(RoundedCornerShape(8.dp))
//            .padding(8.dp)
            .width(100.dp),
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
        AnimatedVisibility(visible = isExpBarVisible) {
            LinearProgressIndicator(
                progress = { exp.toFloat() / expToNextLevel },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .clip(RoundedCornerShape(8.dp)),
                color = AccentColor,
                trackColor = Color.White,
            )
        }
    }
}