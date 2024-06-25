package com.example.explory.presentation.screen.map.note

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.explory.R
import com.example.explory.domain.model.MapNote
import com.example.explory.presentation.screen.auth.component.LoadingItem
import com.example.explory.presentation.screen.auth.onboarding.component.PageIndicator
import com.example.explory.presentation.screen.quest.ImagePage
import com.example.explory.presentation.screen.quest.InfoBox
import com.example.explory.ui.theme.Black
import com.example.explory.ui.theme.S16_W600
import com.example.explory.ui.theme.White
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteSheet(
    mapNote: MapNote,
    onDismissRequest: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { onDismissRequest() }
    ) {
        when (mapNote.note) {
            null -> {
                LoadingItem()
            }

            else -> {
                val images = mapNote.note.images
                val pagerState = rememberPagerState(pageCount = { images.size })
                Box(Modifier.fillMaxWidth()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        item {
                            Text(
                                text = mapNote.note.note,
                                style = S16_W600,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        item {
                            Column(modifier = Modifier.padding(4.dp)) {
//                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {

//                            InfoBox(text = note.profile.username)
//                        }
                                InfoBox(text = formatDateTime(mapNote.note.createdAt))
//                                Spacer(modifier = Modifier.height(4.dp))
//                                InfoBox(text = "${mapNote.note.latitude}, ${mapNote.note.longitude}")
                            }
                        }

                        item {
                            if (images.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(16.dp))
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
                            } else {
                                Spacer(modifier = Modifier.height(16.dp))
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

    }
}

fun formatDateTime(input: String): String {
    val zonedDateTime = ZonedDateTime.parse(input)
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
    return zonedDateTime.format(formatter)
}