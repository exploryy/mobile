package com.example.explory.presentation.screen.battlepass.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.explory.ui.theme.Green
import com.example.explory.ui.theme.S20_W600
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun CountdownTimer(targetDateTime: String) {
    var remainingTime by remember { mutableStateOf<Duration?>(null) }
    val dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME
    val targetTime = LocalDateTime.parse(targetDateTime, dateTimeFormatter)

    LaunchedEffect(Unit) {
        while (true) {
            val currentTime = LocalDateTime.now()
            remainingTime = Duration.between(currentTime, targetTime)
            delay(1000L)
        }
    }

    remainingTime?.let {
        val days = it.toDays()
        val hours = it.toHours() % 24
        val minutes = it.toMinutes() % 60
        val seconds = it.seconds % 60

        Text(
            text = String.format(
                Locale.getDefault(),
                "%02d дней %02d:%02d:%02d", days, hours, minutes, seconds
            ),
            style = S20_W600,
            color = Green
        )
    }
}