package com.example.explory.presentation.screen.shop

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.explory.R
import com.example.explory.data.model.shop.CosmeticItemInShopDto
import com.example.explory.data.model.shop.CosmeticType
import com.example.explory.data.model.shop.RarityType
import com.example.explory.presentation.screen.shop.component.CategorySelectionRow
import com.example.explory.presentation.screen.shop.component.CosmeticItemsList
import com.example.explory.presentation.screen.shop.component.HorizontalPagerImages
import com.example.explory.ui.theme.S20_W600
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopScreen(
    viewModel: ShopViewModel = koinViewModel(),
    onDismiss: () -> Unit
) {
    val categories = listOf("Все", "FOOTPRINT", "AVATAR_FRAMES", "APPLICATION_IMAGE", "FOG")

    val shopState by viewModel.shopState.collectAsState()

    val filteredCosmeticItems = remember {
        derivedStateOf {
            if (shopState.selectedCategory == "Все") {
                shopState.shopList
            } else {
                shopState.shopList.filter { it.cosmeticType.name == shopState.selectedCategory }
            }
        }
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Магазин",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Image(
                        painter = painterResource(id = R.drawable.money),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = shopState.balance.toString(),
                        color = Color.White,
                        style = S20_W600,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }

            HorizontalPagerImages()

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Категории товаров",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            CategorySelectionRow(
                categories = categories,
                selectedCategory = shopState.selectedCategory,
                onCategorySelected = { category ->
                    viewModel.selectCategory(category)
                }
            )

            CosmeticItemsList(cosmeticItems = filteredCosmeticItems.value)
        }
    }
}


//для тестов
val dummyCosmeticItems = listOf(
    CosmeticItemInShopDto(
        itemId = 1,
        name = "Cool Footprint",
        description = "Leave cool footprints wherever you go!",
        price = 100,
        rarityType = RarityType.RARE,
        cosmeticType = CosmeticType.FOOTPRINT,
        isOwned = false,
        sellable = true,
        url = "string"
    ),
    CosmeticItemInShopDto(
        itemId = 2,
        name = "Epic Avatar Frames",
        description = "Frame your avatar with epic style!",
        price = 200,
        rarityType = RarityType.EPIC,
        cosmeticType = CosmeticType.AVATAR_FRAMES,
        isOwned = true,
        sellable = true,
        url = "string"
    ),
    CosmeticItemInShopDto(
        itemId = 3,
        name = "Mystical Fog",
        description = "Surround yourself with mystical fog.",
        price = 150,
        rarityType = RarityType.COMMON,
        cosmeticType = CosmeticType.FOG,
        isOwned = false,
        sellable = true,
        url = "string"
    ),
    CosmeticItemInShopDto(
        itemId = 4,
        name = "Application Image",
        description = "Customize your application with this image.",
        price = 80,
        rarityType = RarityType.LEGENDARY,
        cosmeticType = CosmeticType.APPLICATION_IMAGE,
        isOwned = false,
        sellable = true,
        url = "string"
    )
)