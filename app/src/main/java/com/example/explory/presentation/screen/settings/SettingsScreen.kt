package com.example.explory.presentation.screen.settings

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.example.explory.data.storage.ThemePreferenceManager
import kotlinx.coroutines.launch
import org.koin.compose.koinInject


// todo fix ))) (произошёл анлак)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    themePreferenceManager: ThemePreferenceManager = koinInject<ThemePreferenceManager>(),
    onBackClick: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    val isDarkTheme = themePreferenceManager.isDarkTheme()

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
                text = "Настройки",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = "Выбор темы",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = { themePreferenceManager.setDarkTheme(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        uncheckedThumbColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        }
    }
}