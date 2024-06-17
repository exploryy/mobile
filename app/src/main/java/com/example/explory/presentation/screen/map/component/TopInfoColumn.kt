package com.example.explory.presentation.screen.map.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.explory.R
import com.example.explory.ui.theme.AccentColor
import com.example.explory.ui.theme.S16_W600
import com.example.explory.ui.theme.S20_W600
import com.example.explory.ui.theme.S24_W600
import com.example.explory.ui.theme.White

@Composable
fun TopInfoColumn(
    modifier: Modifier = Modifier,
    currentLocationName: String = "Локация",
    currentLocationPercent: Double = 0.0,
    coinCount: Int = 0,
    currentLevel: Int = 1,
    exp: Int = 0,
    expToNextLevel: Int = 100
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.width(165.dp)) {
            Text(
                text = currentLocationName.lowercase(),
                color = Color.White,
                style = S24_W600,
                textDecoration = TextDecoration.Underline,

                )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${"%.4f".format(currentLocationPercent)}%", color = Color.White,
                style = S20_W600,
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        BalanceBar(
            balance = coinCount,
            level = currentLevel,
            exp = exp,
            expToNextLevel = expToNextLevel
        )
    }
}

@Composable
fun BalanceBar(
    modifier: Modifier = Modifier, balance: Int, level: Int, exp: Int, expToNextLevel: Int
) {
    Column(
        modifier = modifier
            .background(White.copy(0.1f), shape = RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .padding(8.dp)
            .width(120.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            BarItem(value = balance, icon = R.drawable.coin)
            Spacer(modifier = Modifier.weight(1f))
            BarItem(value = level, icon = R.drawable.star)
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { exp.toFloat() / expToNextLevel },
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp)),
            color = AccentColor,
            trackColor = Color.White,
        )
    }
}

@Composable
fun BarItem(modifier: Modifier = Modifier, value: Int, icon: Int) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "$value",
            color = Color.White,
            style = S16_W600,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
    }
}