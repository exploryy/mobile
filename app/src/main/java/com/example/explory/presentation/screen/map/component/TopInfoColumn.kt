package com.example.explory.presentation.screen.map.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.explory.R
import com.example.explory.ui.theme.S20_W600
import com.example.explory.ui.theme.S24_W600

@Composable
fun TopInfoColumn(
    modifier: Modifier = Modifier,
    currentLocationName: String = "Локация",
    currentLocationPercent: Double = 0.0,
    coinCount: Int = 0,
) {
    Column(modifier = modifier) {
        Text(
            text = currentLocationName.lowercase(),
            color = Color.White,
            style = S24_W600,
            textDecoration = TextDecoration.Underline,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "$currentLocationPercent%",
            color = Color.White,
            style = S20_W600,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.money),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "$coinCount",
                color = Color.White,
                style = S20_W600,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}