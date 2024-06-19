package com.example.explory.presentation.screen.quest

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.explory.R
import com.example.explory.data.model.quest.PointDto
import com.example.explory.presentation.screen.auth.onboarding.component.PageIndicator
import com.example.explory.presentation.screen.map.component.BarItem
import com.example.explory.ui.theme.Black
import com.example.explory.ui.theme.DarkGray
import com.example.explory.ui.theme.ExploryTheme
import com.example.explory.ui.theme.Gray
import com.example.explory.ui.theme.Green
import com.example.explory.ui.theme.S12_W600
import com.example.explory.ui.theme.S14_W600
import com.example.explory.ui.theme.S16_W400
import com.example.explory.ui.theme.S16_W600
import com.example.explory.ui.theme.S20_W600
import com.example.explory.ui.theme.White

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun QuestSheet(
    name: String,
    images: List<String>,
    point: PointDto,
    description: String,
    difficulty: String,
    transportType: String,
    distance: Double,
    onButtonClicked: () -> Unit,
    onDismissRequest: () -> Unit,
    reviews: ReviewsDto?,
    questStatus: String? = null
) {
    val state = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            confirmValueChange = {
                if (it == SheetValue.Hidden) {
                    onDismissRequest()
                }
                true
            },
            skipHiddenState = false
        )
    )
    val pagerState = rememberPagerState(pageCount = { images.size })
    BottomSheetScaffold(
        sheetPeekHeight = 250.dp,
        scaffoldState = state, sheetContent = {
            Box(Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ImagePage(
                                image = images.firstOrNull(),
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(
                                Modifier.padding(end = 64.dp)
                            ) {
                                Text(
                                    text = name,
                                    style = S20_W600,
                                )
                                if (reviews != null) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row {
                                        Text(
                                            text = "${reviews.avgRating}",
                                            style = S16_W400,
                                            color = White
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            painter = painterResource(id = R.drawable.star),
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            InfoBox(text = "${point.latitude}, ${point.longitude}")
                            if (questStatus != null) {
                                InfoBox(text = questStatus, containerColor = Green)
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))
                        Text(text = description, style = S16_W400, color = Gray)

                        Spacer(modifier = Modifier.height(18.dp))
                        FlowRow(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            InfoBox(
                                text = transportType
                            )
                            InfoBox(text = "${distance.toInt()} метров")
                            InfoBox(text = difficulty)
                            InfoBox(text = "одиночный")
                        }
                    }


                    item {
                        Spacer(modifier = Modifier.height(40.dp))
                        Button(
                            shape = RoundedCornerShape(8.dp),
                            onClick = { onButtonClicked() },
                            modifier = Modifier
                                .height(50.dp)
                                .fillMaxWidth(),

                            colors = ButtonDefaults.buttonColors(
                                contentColor = Black, containerColor = White
                            )
                        ) {
                            Text(
                                if (questStatus == null) "Начать квест" else "Отменить квест",
                                style = S14_W600
                            )
                        }
                    }
                    item {
                        if (images.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(32.dp))
                            HorizontalPager(state = pagerState) { page ->
                                ImagePage(
                                    image = images[page],
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            PageIndicator(pagerState = pagerState)
                        }
                    }

                    if (reviews?.list?.isNotEmpty() == true) {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "Отзывы", style = S20_W600)
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        items(reviews.list) {
                            ReviewCard(review = it)
                        }
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }

                }
                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Black,
                        containerColor = White
                    ),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .clip(CircleShape)
                        .padding(end = 24.dp),

                    onClick = {
                        Log.d("QuestSheet", "Clicked button")
                        onDismissRequest()
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.close),
                        contentDescription = null,
                        tint = Black,
                    )
                }
            }
        }) {}
}

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
                    painter = painterResource(id = R.drawable.picture), contentDescription = null,
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

@Composable
fun ReviewCard(modifier: Modifier = Modifier, review: ReviewDto) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = review.author,
                style = S16_W600,
                color = White
            )
            Spacer(modifier = Modifier.weight(1f))
            BarItem(value = review.rating, icon = R.drawable.star, textColor = White)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = review.text,
            style = S16_W400,
            color = Gray,
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (review.images.isNotEmpty()) {
            val sidePadding = (-16).dp
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.layout { measurable, constraints ->
                    val placeable =
                        measurable.measure(constraints.offset(horizontal = -sidePadding.roundToPx() * 2))

                    layout(
                        placeable.width + sidePadding.roundToPx() * 2,
                        placeable.height
                    ) {
                        placeable.place(+sidePadding.roundToPx(), 0)
                    }

                }
            ) {
                items(review.images) { image ->
                    ImagePage(
                        image = image,
                        modifier = Modifier
                            .size(100.dp),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = review.date,
            style = S12_W600,
            color = Gray,
        )
    }
}

@Preview
@Composable
private fun PreviewReviewCard() {
    ReviewCard(
        review = ReviewDto(
            author = "Андрейка",
            date = "21.01.2024",
            rating = 5,
            text = "Местечно так то конечно классное, но не хватает пива и чипсов, вообще задание классное было, весело провёл время разработчики они вообще молодцы реально плюс репчик им!!",
            images = listOf(
                "https://i.imgur.com/oFc85y7.jpeg",
                "https://i.imgur.com/eCS5ghi.jpeg",
                "https://i.imgur.com/YsPlS5K.jpeg"
            )
        )
    )
}

@Preview
@Composable
private fun PreviewQuestSheet() {
    ExploryTheme {
        QuestSheet(name = "Длинное название квеста квест квест квест",
            images = emptyList(),
            point = PointDto("0.0", "0.0", "0.0", "0.0"),
            description = "Описание",
            difficulty = "Сложность",
            transportType = "Тип транспорта",
            distance = 100.0,
            onButtonClicked = { },
            reviews = null,
            onDismissRequest = { })
    }
}


data class ReviewsDto(
    val avgRating: Double,
    val list: List<ReviewDto>
)

data class ReviewDto(
    val author: String,
    val date: String,
    val rating: Int,
    val text: String,
    val images: List<String>
)