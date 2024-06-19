package com.example.explory.presentation.screen.battlepass.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.explory.data.model.battlepass.BattlePassLevelDto

@Composable
fun LevelCard(level: BattlePassLevelDto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Level ${level.level}", style = MaterialTheme.typography.headlineMedium)
            Text(text = "Experience Needed: ${level.experienceNeeded}", style = MaterialTheme.typography.bodyLarge)

            level.rewards.forEach { reward ->
                RewardItem(reward)
            }
        }
    }
}