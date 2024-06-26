package com.example.explory.presentation.screen.map.component.mapoverlay

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.explory.ui.theme.S20_W600
import com.example.explory.ui.theme.S24_W600
import com.example.explory.ui.theme.White

@Composable
fun TopInfoColumn(
    modifier: Modifier = Modifier,
    currentLocationName: String = "Локация",
    currentLocationPercent: Double = 0.0,
) {
    Column(modifier.width(165.dp)) {
        if (currentLocationName.isBlank()) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = White,
                trackColor = White.copy(0.5f)
            )
        } else {
            Text(
                text = currentLocationName.lowercase(),
                color = White,
                style = S24_W600,
                textDecoration = TextDecoration.Underline
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (currentLocationPercent == 0.0) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = White,
                trackColor = Color.White.copy(0.5f)
            )
        } else {
            Text(
                text = "${"%.4f".format(currentLocationPercent)}%",
                color = White,
                style = S20_W600,
            )
        }

    }
}


