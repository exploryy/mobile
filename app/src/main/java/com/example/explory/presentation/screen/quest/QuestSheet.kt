package com.example.explory.presentation.screen.quest

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.explory.R
import com.example.explory.data.model.quest.FullReviewsDto
import com.example.explory.data.model.quest.PointDto
import com.example.explory.data.model.quest.TransportType
import com.example.explory.presentation.screen.auth.onboarding.component.PageIndicator
import com.example.explory.presentation.common.ImagePage
import com.example.explory.presentation.screen.quest.component.InfoBox
import com.example.explory.presentation.screen.quest.component.ReviewCard
import com.example.explory.presentation.screen.quest.component.SelectTransportDialog
import com.example.explory.ui.theme.Black
import com.example.explory.ui.theme.ExploryTheme
import com.example.explory.ui.theme.Gray
import com.example.explory.ui.theme.Green
import com.example.explory.ui.theme.S14_W600
import com.example.explory.ui.theme.S16_W400
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
    distance: Double,
    onButtonClicked: (TransportType) -> Unit,
    onDismissRequest: () -> Unit,
    reviews: FullReviewsDto,
    questStatus: String? = null,
    isCompleted: Boolean
) {
    val isSelectTransportDialogOpen = remember { mutableStateOf(false) }
    val state = rememberBottomSheetScaffoldState()
    val pagerState = rememberPagerState(pageCount = { images.size })
    BottomSheetScaffold(sheetPeekHeight = 200.dp, scaffoldState = state, sheetContent = {
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
                            if (reviews.reviews.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Row {
                                    Text(
                                        text = reviews.avg.toString(),
                                        style = S16_W400,
                                        color = White
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Image(
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
                        InfoBox(
                            text = "${point.latitude}, ${point.longitude}",
                            isCopyingEnabled = true
                        )
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
//                        InfoBox(
//                            text = transportType
//                        )
                        InfoBox(text = "${distance.toInt()} метров")
                        InfoBox(text = difficulty)
                        InfoBox(text = "одиночный")
                    }
                }

                if (!isCompleted){
                    item {
                        Spacer(modifier = Modifier.height(40.dp))
                        Button(
                            shape = RoundedCornerShape(8.dp),
                            onClick = {
                                if (questStatus == null) {
                                    isSelectTransportDialogOpen.value = true
                                } else {
                                    onButtonClicked(TransportType.WALK)
                                }
                            },
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
                }

                item {
                    if (images.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(32.dp))
                        HorizontalPager(state = pagerState) { page ->
                            ImagePage(
                                image = images[page],
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        PageIndicator(pagerState = pagerState)
                    }
                }

                if (reviews.reviews.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "отзывы", style = S20_W600)
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    items(reviews.reviews) {
                        ReviewCard(review = it)
                    }
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }

            }
            IconButton(colors = IconButtonDefaults.iconButtonColors(
                contentColor = Black, containerColor = White
            ),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clip(CircleShape)
                    .padding(end = 24.dp),

                onClick = {
                    onDismissRequest()
                }) {
                Icon(
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = null,
                    tint = Black,
                )
            }
        }
    }) {}

    if (isSelectTransportDialogOpen.value) {
        SelectTransportDialog(
            onDismissRequest = { isSelectTransportDialogOpen.value = false },
            onStartQuest = onButtonClicked
        )
    }
}


@Preview
@Composable
private fun PreviewQuestSheet() {
    ExploryTheme {
        QuestSheet(
            name = "Длинное название квеста квест квест квест",
            images = emptyList(),
            point = PointDto("0.0", "0.0"),
            description = "Описание",
            difficulty = "Сложность",
            distance = 100.0,
            onButtonClicked = { },
            onDismissRequest = { },
            reviews = FullReviewsDto(
                avg = 4.5,
                reviews = emptyList()
            ),
            isCompleted = false
        )
    }
}