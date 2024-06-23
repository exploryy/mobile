package com.example.explory.presentation.screen.settings.component

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    state: Boolean,
    @DrawableRes initIcon: Int,
    @DrawableRes targetIcon: Int
) {
    val rotationAngle by animateFloatAsState(targetValue = if (state) 360f else 0f, label = "")
    val iconId = if (state) targetIcon else initIcon

    Box(
        modifier = modifier
            .size(32.dp),
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = { onClick() }) {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(28.dp)
                    .graphicsLayer(rotationZ = rotationAngle),
            )
        }
    }
}