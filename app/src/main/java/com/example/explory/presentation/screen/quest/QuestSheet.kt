package com.example.explory.presentation.screen.quest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.explory.R
import com.example.explory.data.model.quest.PointDto
import com.example.explory.ui.theme.Black
import com.example.explory.ui.theme.DarkGray
import com.example.explory.ui.theme.Green
import com.example.explory.ui.theme.S14_W600
import com.example.explory.ui.theme.S16_W400
import com.example.explory.ui.theme.S24_W600
import com.example.explory.ui.theme.White
import com.skydoves.flexible.bottomsheet.material3.FlexibleBottomSheet
import com.skydoves.flexible.core.FlexibleSheetSize
import com.skydoves.flexible.core.rememberFlexibleBottomSheetState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuestSheet(
    name: String,
    image: String?,
    point: PointDto,
    description: String,
    difficulty: String,
    transportType: String,
    distance: Double,
    onButtonClicked: () -> Unit,
    questStatus: String? = null
) {
    FlexibleBottomSheet(
        onDismissRequest = { },
        windowInsets = WindowInsets(0.dp),
        sheetState = rememberFlexibleBottomSheetState(
            flexibleSheetSize = FlexibleSheetSize(
                fullyExpanded = 0.9f,
                intermediatelyExpanded = 0.5f,
                slightlyExpanded = 0.12f,
            ),
            allowNestedScroll = false,
            isModal = false,
            skipIntermediatelyExpanded = true,
            skipSlightlyExpanded = false,
            skipHiddenState = true,
        ),
        containerColor = DarkGray,
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(image)
                        .crossfade(true)
                        .build(),
                    loading = {
                        CircularProgressIndicator()
                    },
                    error = {
                        Icon(
                            painter = painterResource(id = R.drawable.picture),
                            contentDescription = null
                        )
                    },
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = name,
                        style = S24_W600,
                    )
                    if (questStatus != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        InfoBox(text = questStatus, containerColor = Green)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                InfoBox(text = "${point.latitude}, ${point.longitude}")
            }

            Spacer(modifier = Modifier.height(18.dp))
            Text(text = description, style = S16_W400)
            Spacer(modifier = Modifier.height(18.dp))
            FlowRow(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InfoBox(
                    text = transportType
                )
                InfoBox(text = "$distance метров")
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
                    contentColor = Black,
                    containerColor = White
                )
            ) {
                Text(
                    if (questStatus == null) "Начать квест" else "Отменить квест",
                    style = S14_W600
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}