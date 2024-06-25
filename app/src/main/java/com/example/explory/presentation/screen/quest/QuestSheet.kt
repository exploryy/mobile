package com.example.explory.presentation.screen.quest

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.elevatedCardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.rememberAsyncImagePainter
import com.example.explory.R
import com.example.explory.data.model.quest.FullReviewsDto
import com.example.explory.data.model.quest.PointDto
import com.example.explory.presentation.screen.auth.onboarding.component.PageIndicator
import com.example.explory.ui.theme.Black
import com.example.explory.ui.theme.Gray
import com.example.explory.ui.theme.Green
import com.example.explory.ui.theme.S14_W600
import com.example.explory.ui.theme.S16_W400
import com.example.explory.ui.theme.S20_W600
import com.example.explory.ui.theme.White
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

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
    reviews: FullReviewsDto,
    questStatus: String? = null
) {
    // todo выбор транспорта
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
                        onClick = {
                            if (questStatus == null) {
                                isSelectTransportDialogOpen.value = true
                            } else {
                                onButtonClicked()
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

@Composable
fun SelectTransportDialog(onDismissRequest: () -> Unit, onStartQuest: () -> Unit) {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(
            dismissOnBackPress = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onBackground)
        ) {
            val pagerState = rememberPagerState(pageCount = { 3 })
            HorizontalPager(
                pageSpacing = 16.dp,
                beyondViewportPageCount = 2,
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                Box(modifier = Modifier.fillMaxSize()) {
                    TransportInformationCard(
                        modifier = Modifier
                            .padding(32.dp)
                            .align(Alignment.Center),
                        pagerState = pagerState,
                        page = page
                    )
                }

            }
        }
    }
}

@Composable
fun TransportInformationCard(
    pagerState: PagerState,
    page: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .shadow(16.dp, ambientColor = MaterialTheme.colorScheme.onBackground),
        shape = RoundedCornerShape(32.dp),
        colors = elevatedCardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier) {
            val pageOffset = pagerState.getOffsetDistanceInPages(page).absoluteValue
            Log.d("dribble", "Page: $page pageOffset $pageOffset")
            Image(
                modifier = Modifier
                    .padding(32.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .aspectRatio(1f)
                    .background(Color.LightGray)
                    .graphicsLayer {
                        // get a scale value between 1 and 1.75f, 1.75 will be when its resting,
                        // 1f is the smallest it'll be when not the focused page
                        val scale = lerp(1f, 1.75f, pageOffset)
                        // apply the scale equally to both X and Y, to not distort the image
                        scaleX *= scale
                        scaleY *= scale
                    },
                painter = rememberAsyncImagePainter(
                    model = "https://i.imgur.com/YsPlS5K.jpeg",
                ),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
            SongDetails()
            DragToListen(pageOffset)
        }
    }
}

@Composable
private fun DragToListen(pageOffset: Float) {
    Box(
        modifier = Modifier
            .height(150.dp * (1 - pageOffset))
            .fillMaxWidth()
            .graphicsLayer {
                alpha = 1 - pageOffset
            }
    ) {
        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Rounded.MusicNote, contentDescription = "",
                modifier = Modifier
                    .padding(8.dp)
                    .size(36.dp)
            )
            Text("DRAG TO LISTEN")
            Spacer(modifier = Modifier.size(4.dp))
            DragArea()
        }
    }
}

@Composable
private fun DragArea() {
    Box {
        Canvas(
            modifier = Modifier
                .padding(0.dp)
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(bottomEnd = 32.dp, bottomStart = 32.dp))
        ) {
            val sizeGap = 16.dp.toPx()
            val numberDotsHorizontal = size.width / sizeGap + 1
            val numberDotsVertical = size.height / sizeGap + 1
            repeat(numberDotsHorizontal.roundToInt()) { horizontal ->
                repeat(numberDotsVertical.roundToInt()) { vertical ->
                    drawCircle(
                        Color.LightGray.copy(alpha = 0.5f), radius = 2.dp.toPx
                            (), center =
                        Offset(horizontal * sizeGap + sizeGap, vertical * sizeGap + sizeGap)
                    )
                }
            }
        }
        Icon(
            Icons.Rounded.ExpandMore, "down",
            modifier = Modifier
                .size(height = 24.dp, width = 48.dp)
                .align(Alignment.Center)
                .background(Color.White)
        )
    }
}

@Composable
private fun SongDetails() {
    Spacer(modifier = Modifier.padding(8.dp))
    Text(
        "Artist",
        fontSize = 16.sp,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.padding(4.dp))
    Text(
        "A Song Title",
        fontSize = 24.sp,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.padding(8.dp))
    StarRating(3)
}

@Composable
fun StarRating(stars: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        val yellowColor = Color(0xFFFF9800)
        for (i in 0 until stars) {
            Icon(
                Icons.Rounded.Star,
                contentDescription = "Star",
                tint = yellowColor,
                modifier = Modifier.size(36.dp)
            )
        }
        for (i in 0 until (5 - stars)) {
            Icon(
                Icons.Rounded.Star, contentDescription = "Star empty",
                modifier = Modifier.size(36.dp),
                tint = yellowColor.copy(alpha = 0.25f)
            )
        }
    }
}
