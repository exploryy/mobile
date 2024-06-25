package com.example.explory.presentation.screen.battlepass.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.explory.data.model.battlepass.BattlePassLevelDto
import com.example.explory.ui.theme.BattlePass
import com.example.explory.ui.theme.S20_W600
import kotlin.math.min


@Composable
fun BattlePassItem(
    quest: BattlePassLevelDto,
    currentLevel: Int,
    currentExp: Int,
    prevLevelExp: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(
                if (quest.level <= currentLevel) BattlePass.copy(alpha = 0.4f) else Color.Transparent,
            )
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = quest.level.toString(),
                style = S20_W600,
                modifier = Modifier
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        val progress = when {
//            nextLevelExp == -1 -> if (quest.level == currentLevel) 100f else 0f
            quest.experienceNeeded <= currentExp -> 100f
            quest.level - 1 == currentLevel -> {
                min(
                    (currentExp.toFloat() / (quest.experienceNeeded - prevLevelExp).toFloat()),
                    100f
                )
            }


            else -> 0f
        }
        Log.d(
            "BattlePassItem",
            "level ${quest.level} progress $progress this level exp ${quest.experienceNeeded} current exp $currentExp next level exp $prevLevelExp"
        )
        CustomBar(
            progress = progress,
            backgroundColor = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.weight(1f))
        if (quest.rewards.isNotEmpty()) {
            BattlePassRewardItem(
                item = quest.rewards.first(),
                modifier = Modifier.size(80.dp),
                isCollected = quest.level <= currentLevel
            )
        } else {
            BattlePassRewardItem(
                item = null,
                modifier = Modifier.size(80.dp),
                isCollected = quest.level <= currentLevel
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview
@Composable
private fun PreviewItem() {
    BattlePassItem(
        quest = BattlePassLevelDto(
            level = 1,
            experienceNeeded = 2000,
            rewards = emptyList()
        ),
        currentLevel = 0,
        currentExp = 1050,
        prevLevelExp = 0
    )
}