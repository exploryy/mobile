package com.example.explory.presentation.screen.settings.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.explory.R

@Composable
fun ConfidentialityButton(
    onClick: (Boolean) -> Unit,
    isConfidentially: Boolean
) {
    val iconId = R.drawable.like
    val rotationAngle by animateFloatAsState(targetValue = if (!isConfidentially) 180f else 0f, label = "")

    Box(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.surface)
            .size(32.dp),
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = { onClick(!isConfidentially) }) {
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