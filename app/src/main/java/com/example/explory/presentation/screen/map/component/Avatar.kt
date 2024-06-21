package com.example.explory.presentation.screen.map.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.example.explory.R
import com.example.explory.data.model.inventory.CosmeticItemInInventoryDto
import com.example.explory.ui.theme.DarkGray
import com.example.explory.ui.theme.White

@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    image: String?,
    border: CosmeticItemInInventoryDto? = null
) {
    val ctx = LocalContext.current
    val imageLoader = ImageLoader.Builder(ctx).build()

    Box(
        modifier = modifier
    ) {
        Box(
            modifier = modifier
                .background(color = DarkGray, shape = CircleShape)
//                .padding(4.dp)
        ) {
            SubcomposeAsyncImage(
                model = image,
                imageLoader = imageLoader,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .align(Alignment.Center)
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
        }

        border?.let {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
            ) {
                SubcomposeAsyncImage(
                    model = it.url,
                    imageLoader = imageLoader,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize(),
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
                        is AsyncImagePainter.State.Empty -> {}
                        is AsyncImagePainter.State.Loading -> {}
                    }
                }
            }
        }
    }
}
