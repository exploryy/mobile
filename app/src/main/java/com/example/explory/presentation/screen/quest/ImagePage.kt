package com.example.explory.presentation.screen.quest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.explory.R
import com.example.explory.ui.theme.DarkGray

@Composable
fun ImagePage(
    modifier: Modifier = Modifier,
    image: String?,
    shape: RoundedCornerShape = RoundedCornerShape(0.dp)
) {
    Box(modifier = modifier.background(DarkGray, shape)) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(image).crossfade(true).build(),
            loading = {
                CircularProgressIndicator()
            },
            error = {
                Icon(
                    painter = painterResource(id = R.drawable.picture),
                    contentDescription = null,
                    modifier = Modifier.scale(0.5f)
                )

            },
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .fillMaxSize()
                .clip(shape)
        )
    }
}