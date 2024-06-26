package com.example.explory.presentation.screen.shop.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.explory.data.model.shop.CosmeticItemInShopDto

@Composable
fun CosmeticItemsList(
    cosmeticItems: List<CosmeticItemInShopDto>,
    onItemClick: (CosmeticItemInShopDto) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        itemsIndexed(cosmeticItems) { _, cosmeticItem ->
            CosmeticCard(
                cosmeticItem = cosmeticItem,
                onClick = { onItemClick(it) },
                modifier = Modifier
            )
        }
    }
}
