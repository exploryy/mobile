package com.example.explory.presentation.screen.battlepass.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.explory.data.model.battlepass.BattlePassLevelDto

@Composable
fun BattlePassContent(items: List<BattlePassLevelDto>, currentLevel: Int, currentExp: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .border(10.dp, MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))

    ) {
        items.forEachIndexed { index, _ ->
            BattlePassItem(
                items[index],
                currentLevel,
                currentExp,
                if (index == items.size - 1) -1 else items[index + 1].experienceNeeded
            )
            HorizontalDivider(
                modifier = Modifier.clip(RoundedCornerShape(8.dp)),
                thickness = 10.dp,
                color = MaterialTheme.colorScheme.secondary
            )
        }

    }
}