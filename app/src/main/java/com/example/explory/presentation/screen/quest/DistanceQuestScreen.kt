package com.example.explory.presentation.screen.quest

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.explory.R
import com.example.explory.ui.theme.Black
import com.example.explory.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DistanceQuestScreen(
    name: String,
    description: String,
    difficulty: String,
    transportType: String,
    distance: Long,
    image: String?,
    onNavigateBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    Box(modifier = Modifier.fillMaxSize()) {
        QuestImage(image = image, modifier = Modifier.fillMaxHeight(0.3f))
        ModalBottomSheet(
            modifier = Modifier.fillMaxHeight(0.7f),
            sheetState = sheetState,
            onDismissRequest = { onNavigateBack() },
            containerColor = Black,

            contentColor = White
        ) {
            Column {
                Text(text = name)
                Text(text = description)
                Text(text = "Сложность $difficulty")
                Text(text = "Тип транспорта $transportType")
                Text(text = "Расстояние $distance")
            }
        }
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.back),
                tint = White,
                contentDescription = null
            )
        }
    }
}
