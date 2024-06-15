package com.example.explory.presentation.screen.shop.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.explory.data.model.shop.CosmeticItemInShopDto

@Composable
fun CosmeticItemsList(
    cosmeticItems: List<CosmeticItemInShopDto>,
    onItemClick: (CosmeticItemInShopDto) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val itemWidth = screenWidth / 7 * 3 + 40.dp

    LazyRow(
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp)
    ) {
        itemsIndexed(cosmeticItems) { index, cosmeticItem ->
            CosmeticCard(
                cosmeticItem = cosmeticItem,
                onClick = { onItemClick(it) },
                modifier = Modifier
                    .width(itemWidth)
            )
        }
    }
}
