package com.example.explory.presentation.screen.inventory.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.explory.data.model.inventory.CosmeticItemInInventoryDto
import com.example.explory.presentation.screen.shop.getRarityColor
import com.example.explory.presentation.screen.shop.getTranslateRareName

@Composable
fun InventoryItemCard(
    item: CosmeticItemInInventoryDto,
    onEquipClick: () -> Unit,
    onSellClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth()
            .clickable(enabled = !item.isEquipped) {

            },
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = item.itemId,
                contentDescription = null,
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = item.name, style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Редкость: ${getTranslateRareName(item.rarityType)}",
                style = MaterialTheme.typography.bodyMedium,
                color = getRarityColor(item.rarityType)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                TextButton(
                    onClick = onSellClick,
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                ) {
                    Text("Продать")
                }

                Spacer(modifier = Modifier.width(8.dp))

                TextButton(
                    onClick = onEquipClick,
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Green),
                    enabled = !item.isEquipped
                ) {
                    Text("Экипировать")
                }
            }
        }
    }
}