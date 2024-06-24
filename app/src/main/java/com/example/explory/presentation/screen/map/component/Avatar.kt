package com.example.explory.presentation.screen.map.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.example.explory.R
import com.example.explory.data.model.shop.CosmeticType
import com.example.explory.data.model.shop.RarityType
import com.example.explory.ui.theme.White
import com.example.explory.data.model.inventory.CosmeticItemInInventoryDto as CosmeticItemInInventoryDto1

@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    image: String?,
    border: CosmeticItemInInventoryDto1? = null
) {
    val ctx = LocalContext.current
    val imageLoader = ImageLoader.Builder(ctx).build()

    Box(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.surface, shape = CircleShape)
            .scale(if (border != null) 0.85f else 1f),
//                .padding(4.dp)
    ) {
        SubcomposeAsyncImage(
            model = image,
            imageLoader = imageLoader,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape),
            alignment = Alignment.Center
        ) {
            when (painter.state) {
                is AsyncImagePainter.State.Success -> {
                    SubcomposeAsyncImageContent()
                }

                is AsyncImagePainter.State.Error -> {
                    Icon(
                        painter = painterResource(id = R.drawable.picture),
                        contentDescription = null,
                        tint = White,
                        modifier = Modifier.scale(0.5f)
                    )
                }

                is AsyncImagePainter.State.Empty -> {
                    Icon(
                        painter = painterResource(id = R.drawable.picture),
                        contentDescription = null,
                        tint = White,
                        modifier = Modifier.scale(0.5f)
                    )
                }

                is AsyncImagePainter.State.Loading -> {
                    CircularProgressIndicator(
                        color = White,
                        trackColor = White.copy(alpha = 0.3f),
                    )
                }
            }
        }

        border?.let {
            Box(
                modifier = modifier
                    .align(Alignment.Center)
            ) {
                SubcomposeAsyncImage(
                    model = it.url,
                    imageLoader = imageLoader,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(1.25f),
                    alignment = Alignment.Center
                ) {
                    when (painter.state) {
                        is AsyncImagePainter.State.Success -> {
                            SubcomposeAsyncImageContent()
                        }

                        is AsyncImagePainter.State.Error -> {
//                            Icon(
//                                painter = painterResource(id = R.drawable.picture),
//                                contentDescription = null,
//                                tint = White,
//                                modifier = Modifier.scale(0.5f)
//                            )
                        }

                        is AsyncImagePainter.State.Empty -> {}
                        is AsyncImagePainter.State.Loading -> {}
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewAvatar() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Avatar(
            modifier = Modifier.size(100.dp),
            image = "https://i.imgur.com/YsPlS5K.jpeg",
            border = CosmeticItemInInventoryDto1(
                itemId = 1,
                name = "Border",
                description = "Border",
                price = 100,
                rarityType = RarityType.RARE,
                cosmeticType = CosmeticType.AVATAR_FRAMES,
                isEquipped = true,
                isSellable = true,
                url = "https://open-the-world-bucket.storage.yandexcloud.net/cosmetic_item_9?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20240622T191242Z&X-Amz-SignedHeaders=host&X-Amz-Credential=YCAJElQv7pUTWn_lWTr8GxNbm%2F20240622%2Fru-central1%2Fs3%2Faws4_request&X-Amz-Expires=604800&X-Amz-Signature=6c80427c2f5001f8f63a1ca374b58f9b3dacd167b5ef029da6d41419d73f7aad"
            ),
        )
    }
}