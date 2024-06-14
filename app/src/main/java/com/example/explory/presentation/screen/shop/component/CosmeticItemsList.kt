package com.example.explory.presentation.screen.shop.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.explory.data.model.shop.CosmeticItemInShopDto
import com.example.explory.presentation.screen.shop.component.CosmeticCard

@Composable
fun CosmeticItemsList(cosmeticItems: List<CosmeticItemInShopDto>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
    ) {
        itemsIndexed(cosmeticItems) { index, cosmeticItem ->
            CosmeticCard(
                cosmeticItem = cosmeticItem,
                modifier = if (index == 0) {
                    Modifier.padding(end = 8.dp)
                } else if (index == cosmeticItems.size - 1) {
                    Modifier.padding(start = 8.dp)
                } else {
                    Modifier.padding(horizontal = 8.dp)
                }
            )
        }
    }
}