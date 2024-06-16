package com.example.explory.presentation.screen.inventory.component

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.explory.data.model.inventory.CosmeticItemInInventoryDto
import com.example.explory.presentation.screen.common.getRarityColor
import com.example.explory.presentation.screen.common.getTranslateCategoryName
import com.example.explory.presentation.screen.common.getTranslateRareName
import com.example.explory.ui.theme.DarkGreen

@Composable
fun InventoryItemDialog(
    onDismiss: () -> Unit,
    cosmeticItem: CosmeticItemInInventoryDto,
    onEquipClick: (Long) -> Unit,
    onUnEquipClick: (Long) -> Unit,
    onSellClick: (Long) -> Unit,
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Описание предмета",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                AsyncImage(
                    model = cosmeticItem.url,
                    contentDescription = null,
                    modifier = Modifier
                        .size(96.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = cosmeticItem.name,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Редкость: ${getTranslateRareName(cosmeticItem.rarityType)}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = getRarityColor(cosmeticItem.rarityType)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Тип: ${getTranslateCategoryName(cosmeticItem.cosmeticType.name)}",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Цена продажи: ${cosmeticItem.price} монеток",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        modifier = Modifier
                            .weight(1f),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Описание:",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = cosmeticItem.description,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.End
                        )
                        Spacer(modifier = Modifier.width(4.dp))

                        TextButton(
                            onClick = { onSellClick(cosmeticItem.itemId) },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = Color.Red
                            )
                        ) {
                            Text("Продать")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color.White
                        )
                    ) {
                        Text("Закрыть")
                    }
                    Spacer(modifier = Modifier.width(8.dp))

                    if (cosmeticItem.isEquipped) {
                        TextButton(
                            onClick = { onUnEquipClick(cosmeticItem.itemId) },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = Color.Gray
                            )
                        ) {
                            Text("Снять")
                        }
                    }
                    else {
                        TextButton(
                            onClick = { onEquipClick(cosmeticItem.itemId) },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = DarkGreen
                            )
                        ) {
                            Text("Экипировать")
                        }
                    }
                }
            }
        }
    }
}