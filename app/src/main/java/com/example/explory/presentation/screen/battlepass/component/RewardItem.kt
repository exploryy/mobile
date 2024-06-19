package com.example.explory.presentation.screen.battlepass.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.explory.data.model.battlepass.BattlePassRewardDto

@Composable
fun RewardItem(item: BattlePassRewardDto) {
    Row(modifier = Modifier.padding(vertical = 8.dp)) {
        AsyncImage(
            model = item.item.url,
            contentDescription = item.item.name,
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(text = item.item.name, style = MaterialTheme.typography.bodyLarge)
            Text(text = item.item.description, style = MaterialTheme.typography.bodyMedium, fontSize = 12.sp)
        }
    }
}