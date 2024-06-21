package com.example.explory.presentation.screen.settings.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.explory.R

@Composable
fun ThemeButton(
    onClick: () -> Unit,
    darkTheme: Boolean
){
    val iconId = if (darkTheme) R.drawable.sun else R.drawable.moon
    val rotationAngle by animateFloatAsState(targetValue = if (darkTheme) 180f else 0f, label = "")

    Box(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.surface)
            .size(32.dp)
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = { onClick() }) {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(32.dp)
                    .graphicsLayer(rotationZ = rotationAngle),
            )
        }
    }
}