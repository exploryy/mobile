package com.example.explory.presentation.screen.quest

import android.util.Log
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
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
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.explory.R
import com.example.explory.data.model.quest.PointDto
import com.example.explory.ui.theme.Black
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
    transportType: String,
    distance: Double,
    onButtonClicked: () -> Unit,
    onDismissRequest: () -> Unit,
    state: BottomSheetScaffoldState,
    questStatus: String? = null
) {

    Log.d("QuestSheet", "QuestSheet state is $state")
//    val pagerState = rememberPagerState(pageCount = { images.size })
    BottomSheetScaffold(
        sheetPeekHeight = 250.dp,
        scaffoldState = state, sheetContent = {
            Box(Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
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
                            Modifier.padding(end = 32.dp)
                        ) {
                            Text(
                                text = name,
                                style = S20_W600,
                            )
                        }
                    }

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
//                    Spacer(modifier = Modifier.height(32.dp))

//                    HorizontalPager(state = pagerState) { page ->
//                        ImagePage(
//                            image = images[page],
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(200.dp)
//                        )
//                    }
//                    Spacer(modifier = Modifier.height(4.dp))
//                    PageIndicator(
//                        pagerState = pagerState
//                    )
                    Spacer(modifier = Modifier.height(16.dp))
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
fun ImagePage(modifier: Modifier = Modifier, image: String?) {
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
        modifier = modifier.clip(RoundedCornerShape(8.dp))
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PreviewQuestSheet() {
    QuestSheet(name = "Квест",
        images = emptyList(),
        point = PointDto("0.0", "0.0", "0.0", "0.0"),
        description = "Описание",
        difficulty = "Сложность",
        transportType = "Тип транспорта",
        distance = 100.0,
        onDismissRequest = { },
        state = rememberBottomSheetScaffoldState(),
        onButtonClicked = { })
}