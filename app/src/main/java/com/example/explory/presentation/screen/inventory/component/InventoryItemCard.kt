package com.example.explory.presentation.screen.inventory.component

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.explory.data.model.inventory.CosmeticItemInInventoryDto
import com.example.explory.presentation.screen.common.getRarityColor
import com.example.explory.presentation.screen.quest.ImagePage
import com.example.explory.ui.theme.S14_W600
import com.example.explory.ui.theme.S16_W600

@Composable
fun InventoryItemCard(
    item: CosmeticItemInInventoryDto,
    onEquipClick: () -> Unit,
    onUnEquipClick: () -> Unit,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.secondary,
        ),
        border = BorderStroke(
            3.dp, color = getRarityColor(rarityType = item.rarityType)
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
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
            ImagePage(
                image = item.url,
                modifier = Modifier.size(96.dp),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.name,
                style = S14_W600,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (item.isEquipped) {
                        onUnEquipClick()
                    } else {
                        onEquipClick()
                    }
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (item.isEquipped) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (item.isEquipped) "cнять" else "надеть",
                    style = S16_W600,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

