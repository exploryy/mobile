package com.example.explory.presentation.screen.map.component.review

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.explory.R
import com.example.explory.presentation.screen.quest.ImagePage
import com.example.explory.ui.theme.Gray
import com.example.explory.ui.theme.White

@Composable
fun RemovableImage(modifier: Modifier = Modifier, image: String, onRemove: () -> Unit) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Gray)
    ) {
        ImagePage(image = image)
        IconButton(
            onClick = onRemove, modifier = Modifier
                .align(Alignment.TopEnd)
                .size(16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.close),
                contentDescription = null,
                tint = White
            )
        }
    }
}