package com.example.explory.presentation.screen.map.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.explory.ui.theme.S16_W600
import com.example.explory.ui.theme.White

@Composable
fun RewardBox(modifier: Modifier = Modifier, count: Int, icon: Int) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(16.dp))
            .background(White.copy(0.3f), shape = RoundedCornerShape(16.dp))
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.width(60.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
            Text(
                text = count.toString(),
                style = S16_W600
            )
        }
    }
}