package com.example.explory.presentation.screen.shop

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.explory.data.model.shop.CosmeticItemInShopDto
import com.example.explory.data.model.shop.CosmeticType
import com.example.explory.data.model.shop.RarityType
import com.example.explory.presentation.common.RoundedSquareAvatar
import com.example.explory.presentation.common.getRarityColor
import com.example.explory.presentation.common.getTranslateRareName
import com.example.explory.ui.theme.S16_W400
import com.example.explory.ui.theme.S16_W600
import com.example.explory.ui.theme.Yellow

@Composable
fun BuyItemDialog(
    onDismiss: () -> Unit,
    onBuyClick: () -> Unit,
    cosmeticItem: CosmeticItemInShopDto
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

                Spacer(modifier = Modifier.height(8.dp))
                RoundedSquareAvatar(
                    image = cosmeticItem.url,
                    modifier = Modifier
                        .size(96.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = cosmeticItem.name,
                    style = S16_W600
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = cosmeticItem.description,
                    style = S16_W400,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "${getTranslateRareName(cosmeticItem.rarityType)} предмет",
                    style = S16_W600,
                    color = getRarityColor(cosmeticItem.rarityType)
                )


                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${cosmeticItem.price} монеток",
                    style = S16_W600,
                    color = Yellow
                )

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.onBackground
                        )
                    ) {
                        Text("закрыть", style = S16_W600)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(
                        onClick = onBuyClick,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.onError
                        )
                    ) {
                        Text("продать", style = S16_W600)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun BuyItemDialogPreview() {
    BuyItemDialog(
        onDismiss = { },
        onBuyClick = { },
        cosmeticItem = item
    )
}

val item = CosmeticItemInShopDto(
    itemId = 1,
    name = "Cool Footprint",
    description = "Leave cool footprints wherever you go!",
    price = 100,
    rarityType = RarityType.LEGENDARY,
    cosmeticType = CosmeticType.FOOTPRINT,
    isOwned = false,
    sellable = true,
    url = "string"
)

