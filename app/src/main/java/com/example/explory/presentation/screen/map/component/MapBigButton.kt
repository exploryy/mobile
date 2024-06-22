package com.example.explory.presentation.screen.map.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.explory.ui.theme.White

@Composable
fun MapBigButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    @DrawableRes icon: Int,
    tint: Color,
    shape: RoundedCornerShape = RoundedCornerShape(0),
) {
    FloatingActionButton(
        containerColor = White,
        onClick = onClick,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 7.dp
        ),
        modifier = modifier
            .clip(shape)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = tint
        )
    }
}