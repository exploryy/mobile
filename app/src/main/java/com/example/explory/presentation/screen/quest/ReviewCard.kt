package com.example.explory.presentation.screen.quest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import com.example.explory.R
import com.example.explory.data.model.quest.ReviewDto
import com.example.explory.presentation.screen.map.component.Avatar
import com.example.explory.presentation.screen.map.component.BarItem
import com.example.explory.presentation.screen.map.note.formatDateTime
import com.example.explory.ui.theme.Gray
import com.example.explory.ui.theme.S12_W600
import com.example.explory.ui.theme.S16_W400
import com.example.explory.ui.theme.S16_W600
import com.example.explory.ui.theme.White

@Composable
fun ReviewCard(modifier: Modifier = Modifier, review: ReviewDto) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Avatar(
                image = review.profile.avatarUrl,
                modifier = Modifier.size(40.dp),
                border = review.profile.inventoryDto.avatarFrames
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = review.profile.username, style = S16_W600, color = White
            )
            Spacer(modifier = Modifier.weight(1f))
            BarItem(value = review.score, icon = R.drawable.star, textColor = White)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = review.message,
            style = S16_W400,
            color = White,
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (review.reviewPhotos.isNotEmpty()) {
            val sidePadding = (-16).dp
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = -sidePadding),
                modifier = Modifier.layout { measurable, constraints ->
                    val placeable =
                        measurable.measure(constraints.offset(horizontal = -sidePadding.roundToPx() * 2))

                    layout(
                        placeable.width + sidePadding.roundToPx() * 2, placeable.height
                    ) {
                        placeable.place(+sidePadding.roundToPx(), 0)
                    }

                }) {
                items(review.reviewPhotos) { image ->
                    ImagePage(
                        image = image,
                        modifier = Modifier.size(100.dp),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = formatDateTime(review.createdAt),
            style = S12_W600,
            color = Gray,
        )
    }
}