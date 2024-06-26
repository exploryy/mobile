package com.example.explory.presentation.screen.battlepass.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CropFree
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.explory.data.model.battlepass.BattlePassRewardDto
import com.example.explory.data.model.shop.RarityType
import com.example.explory.presentation.common.RoundedSquareAvatar
import com.example.explory.presentation.common.getRarityBrush
import com.example.explory.ui.theme.BattlePass

@Composable
fun BattlePassRewardItem(
    modifier: Modifier = Modifier,
    item: BattlePassRewardDto?,
    isCollected: Boolean = false
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
            .border(
                3.dp,
                if (item == null) getRarityBrush(rarityType = RarityType.COMMON) else
                    getRarityBrush(rarityType = item.item.rarityType),
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (item == null) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Rounded.CropFree,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        } else {
            RoundedSquareAvatar(
                image = item.item.url,
                modifier = Modifier
                    .size(80.dp)
                    .padding(8.dp)
            )
        }

        if (isCollected) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.BottomEnd),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Rounded.Done,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = BattlePass
                )
            }
        }
    }
}