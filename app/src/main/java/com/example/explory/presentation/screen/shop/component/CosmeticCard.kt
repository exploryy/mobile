package com.example.explory.presentation.screen.shop.component

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.explory.data.model.shop.CosmeticItemInShopDto
import com.example.explory.data.model.shop.RarityType
import com.example.explory.presentation.screen.common.RoundedSquareAvatar
import com.example.explory.ui.theme.Blue
import com.example.explory.ui.theme.Gray
import com.example.explory.ui.theme.Green
import com.example.explory.ui.theme.Purple
import com.example.explory.ui.theme.S16_W600
import com.example.explory.ui.theme.Yellow

@Composable
fun CosmeticCard(
    cosmeticItem: CosmeticItemInShopDto,
    onClick: (CosmeticItemInShopDto) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
//        modifier = if (!cosmeticItem.isOwned) {
        modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(
                4.dp,
                brush = getRarityBrush(cosmeticItem.rarityType),
                shape = RoundedCornerShape(8.dp)
            )
            .height(225.dp)
            .let { it.clickable { onClick(cosmeticItem) } },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RoundedSquareAvatar(image = cosmeticItem.url, modifier = Modifier.size(96.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = cosmeticItem.name,
                style = S16_W600,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${cosmeticItem.price} монеток",
                style = S16_W600,
                color = Yellow
            )
        }

        if (cosmeticItem.isOwned) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Green.copy(alpha = 0.4f), shape = RoundedCornerShape(8.dp))
            ) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.TopEnd)
                        .size(32.dp)
                )
            }
        }
    }
}

@Composable
fun getRarityBrush(rarityType: RarityType): Brush {
    val infiniteTransition = rememberInfiniteTransition(label = "")

    val colors = when (rarityType) {
        RarityType.COMMON -> listOf(
            Gray.copy(alpha = 0.8f),
            Gray.copy(alpha = 0.8f)
        )

        RarityType.RARE -> listOf(
            Blue.copy(alpha = 0.8f),
            Blue.copy(alpha = 0.4f)
        )

        RarityType.EPIC -> listOf(
            Purple.copy(alpha = 0.7f),
            Purple.copy(alpha = 0.3f)
        )

        RarityType.LEGENDARY -> listOf(
            Yellow.copy(alpha = 1f),
            Yellow.copy(alpha = 0.3f)
        )
    }

    val animatedColors = infiniteTransition.animateColor(
        initialValue = colors[0],
        targetValue = colors[1],
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    return Brush.linearGradient(
        colors = listOf(animatedColors.value, animatedColors.value.copy(alpha = 0.5f)),
        start = Offset(0f, 1f),
        end = Offset(0f, 0f)
    )
}
