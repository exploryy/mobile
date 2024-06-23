package com.example.explory.presentation.screen.settings

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.explory.R
import com.example.explory.presentation.screen.settings.component.AnimatedButton
import com.example.explory.ui.theme.S16_W600
import com.example.explory.ui.theme.S24_W600
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    isDarkTheme: Boolean,
    isPublic: Boolean,
    onThemeChangeClick: (Boolean) -> Unit,
    onPrivacyChangeClick: (Boolean) -> Unit,
    onBackClick: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            sheetState.show()
        }
    }

    ModalBottomSheet(
        onDismissRequest = { onBackClick() },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                },
        ) {
            Text(
                text = "настройки",
                style = S24_W600,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = "тема карты",
                    style = S16_W600,
                    modifier = Modifier.weight(1f)
                )
                AnimatedButton(
                    onClick = { onThemeChangeClick(!isDarkTheme) },
                    state = isDarkTheme,
                    initIcon = R.drawable.sun,
                    targetIcon = R.drawable.moon
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = "приватность",
                    style = S16_W600,
                    modifier = Modifier.weight(1f)
                )
                AnimatedButton(
                    onClick = { onPrivacyChangeClick(!isPublic) },
                    state = isPublic,
                    initIcon = R.drawable.lock,
                    targetIcon = R.drawable.unlock
                )
            }
        }
    }
}