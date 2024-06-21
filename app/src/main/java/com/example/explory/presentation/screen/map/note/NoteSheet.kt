package com.example.explory.presentation.screen.map.note

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.example.explory.data.model.note.NoteDto
import com.example.explory.data.model.quest.PointDto
import com.example.explory.presentation.screen.auth.onboarding.component.PageIndicator
import com.example.explory.presentation.screen.quest.ImagePage
import com.example.explory.presentation.screen.quest.InfoBox
import com.example.explory.ui.theme.Black
import com.example.explory.ui.theme.DarkGray
import com.example.explory.ui.theme.Green
import com.example.explory.ui.theme.S16_W400
import com.example.explory.ui.theme.S20_W600
import com.example.explory.ui.theme.White
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteSheet(
    note: NoteDto,
    onDismissRequest: () -> Unit,
) {
    val images = note.images
    val sheetState = rememberModalBottomSheetState()
    val pagerState = rememberPagerState(pageCount = { images.size })
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { onDismissRequest() }
    ){
        Box(Modifier.fillMaxWidth()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    Text(
                        text = note.note,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                item{
                    Column(modifier = Modifier.padding(4.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            InfoBox(text = formatDateTime(note.createdAt))
                            InfoBox(text = note.profile.username)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        InfoBox(text = "${note.latitude}, ${note.longitude}")
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
                                    .height(300.dp),
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        PageIndicator(pagerState = pagerState)
                    }
                }
            }
            IconButton(colors = IconButtonDefaults.iconButtonColors(
                contentColor = Black, containerColor = White
            ),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clip(CircleShape)
                    .padding(end = 24.dp),

                onClick = {
                    Log.d("QuestSheet", "Clicked button")
                    onDismissRequest()
                }) {
                Icon(
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = null,
                    tint = Black,
                )
            }
        }
    }
}

fun formatDateTime(input: String): String {
    val zonedDateTime = ZonedDateTime.parse(input)
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
    return zonedDateTime.format(formatter)
}