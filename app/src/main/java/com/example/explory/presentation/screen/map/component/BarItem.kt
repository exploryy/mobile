package com.example.explory.presentation.screen.map.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.explory.common.formatNumber
import com.example.explory.ui.theme.S18_W600
import com.example.explory.ui.theme.Yellow

@Composable
fun BarItem(
    modifier: Modifier = Modifier,
    value: Int,
    icon: Int,
    textColor: Color = Yellow,
    formatValue: Boolean = true
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = if (formatValue) formatNumber(value) else value.toString(),
            color = textColor,
            style = S18_W600,
            maxLines = 1,
//            modifier = Modifier.widthIn(min = 0.dp, max = 55.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
    }
}