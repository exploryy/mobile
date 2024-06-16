package com.example.explory.presentation.screen.inventory.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.explory.data.model.inventory.CosmeticItemInInventoryDto
import com.example.explory.presentation.screen.common.getRarityColor
import com.example.explory.presentation.screen.common.getTranslateRareName
import com.example.explory.ui.theme.DarkGreen

@Composable
fun InventoryItemCard(
    item: CosmeticItemInInventoryDto,
    onEquipClick: () -> Unit,
    onUnEquipClick: () -> Unit,
    onSellClick: () -> Unit,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth()
            .clickable {
                onCardClick()
            },
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = item.url,
                contentDescription = null,
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.name,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Редкость: ${getTranslateRareName(item.rarityType)}",
                style = MaterialTheme.typography.bodyMedium,
                color = getRarityColor(item.rarityType),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = {
                    if (item.isEquipped) {
                        onUnEquipClick()
                    } else {
                        onEquipClick()
                    }
                },
                colors = if (item.isEquipped)
                    ButtonDefaults.buttonColors(containerColor = Color.Gray)
                else ButtonDefaults.buttonColors(containerColor = DarkGreen),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (item.isEquipped) "Снять" else "Экипировать",
                )
            }
        }
    }
}

