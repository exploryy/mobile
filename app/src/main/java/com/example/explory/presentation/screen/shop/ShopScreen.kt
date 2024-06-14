package com.example.explory.presentation.screen.shop

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.explory.R
import com.example.explory.data.model.shop.CosmeticItemInShopDto
import com.example.explory.data.model.shop.CosmeticType
import com.example.explory.data.model.shop.RarityType
import com.example.explory.presentation.screen.friendprofile.component.CategorySelectionRow
import com.example.explory.presentation.screen.friendprofile.component.CosmeticItemsList
import com.example.explory.presentation.screen.map.component.DotIndicator
import com.example.explory.presentation.screen.map.component.HorizontalPagerImages
import com.example.explory.ui.theme.S20_W600

val dummyCosmeticItems = listOf(
    CosmeticItemInShopDto(
        itemId = 1,
        name = "Cool Footprint",
        description = "Leave cool footprints wherever you go!",
        price = 100,
        rarityType = RarityType.RARE,
        cosmeticType = CosmeticType.FOOTPRINT,
        isOwned = false
    ),
    CosmeticItemInShopDto(
        itemId = 2,
        name = "Epic Avatar Frames",
        description = "Frame your avatar with epic style!",
        price = 200,
        rarityType = RarityType.EPIC,
        cosmeticType = CosmeticType.AVATAR_FRAMES,
        isOwned = true
    ),
    CosmeticItemInShopDto(
        itemId = 3,
        name = "Mystical Fog",
        description = "Surround yourself with mystical fog.",
        price = 150,
        rarityType = RarityType.COMMON,
        cosmeticType = CosmeticType.FOG,
        isOwned = false
    ),
    CosmeticItemInShopDto(
        itemId = 4,
        name = "Application Image",
        description = "Customize your application with this image.",
        price = 80,
        rarityType = RarityType.LEGENDARY,
        cosmeticType = CosmeticType.APPLICATION_IMAGE,
        isOwned = false
    )
)

@Composable
fun ShopScreen() {
    val categories = listOf("Все", "FOOTPRINT", "AVATAR_FRAMES", "APPLICATION_IMAGE", "FOG")
    val selectedCategory = remember { mutableStateOf(categories.first()) }
    val filteredCosmeticItems = remember {
        derivedStateOf {
            if (selectedCategory.value == "Все") {
                dummyCosmeticItems
            } else {
                dummyCosmeticItems.filter { it.cosmeticType.name == selectedCategory.value }
            }
        }
    }

    Surface(
        color = Color(0xFF121212),
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
        ) {

            Spacer(modifier = Modifier.height(32.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = "Магазин",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineLarge
                )

                Row {
                    Image(
                        painter = painterResource(id = R.drawable.money),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "85423",
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
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            CategorySelectionRow(
                categories = categories,
                selectedCategory = selectedCategory.value,
                onCategorySelected = { selectedCategory.value = it }
            )
            CosmeticItemsList(cosmeticItems = filteredCosmeticItems.value)
        }
    }
}

fun getPageImage(page: Int): Int {
    return when (page) {
        0 -> R.drawable.cloud
        1 -> R.drawable.cloud2
        2 -> R.drawable.cloud3
        else -> R.drawable.cloud
    }
}


@Composable
fun getIconForCosmeticType(cosmeticType: CosmeticType): Int {
    return when (cosmeticType) {
        CosmeticType.FOOTPRINT -> R.drawable.cloud
        CosmeticType.AVATAR_FRAMES -> R.drawable.cloud
        CosmeticType.APPLICATION_IMAGE -> R.drawable.cloud
        CosmeticType.FOG -> R.drawable.cloud
    }
}

@Composable
fun getRarityColor(rarityType: RarityType): Color {
    return when (rarityType) {
        RarityType.COMMON -> Color.LightGray
        RarityType.RARE -> Color.Green
        RarityType.EPIC -> Color.Magenta
        RarityType.LEGENDARY -> Color.Yellow
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewShopScreen() {
    ShopScreen()
}