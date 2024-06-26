package com.example.explory.presentation.screen.quest.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.explory.R
import com.example.explory.data.model.quest.TransportType
import com.example.explory.domain.model.SelectTransport
import com.example.explory.presentation.common.DotIndicator
import com.example.explory.ui.theme.S24_W600

@Composable
fun SelectTransportDialog(onDismissRequest: () -> Unit, onStartQuest: (TransportType) -> Unit) {
    val transports = listOf(
        SelectTransport(
            "Пешком",
            "Выполнение задания пешком или лёгким бегом",
            R.raw.walk,
            TransportType.WALK
        ),
        SelectTransport(
            "На велосипеде",
            "Выполнение задания на велосипеде, роликах, самокате и так далее",
            R.raw.bike,
            TransportType.BICYCLE
        ),
        SelectTransport(
            "На машине",
            "Выполнение задания на машине, мотоцикле, поезде и так далее",
            R.raw.car,
            TransportType.CAR
        ),
    )
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
                            .align(Alignment.Center)
                            .clickable {
                                onStartQuest(transports[page].transportType)
                                onDismissRequest()
                            },
                        pagerState = pagerState,
                        page = page,
                        transport = transports[page],
                    )
                }
            }

            Text(
                text = "Выберите транспорт",
                style = S24_W600,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 64.dp),
            )

            DotIndicator(
                state = pagerState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 64.dp)
            )


        }
    }
}