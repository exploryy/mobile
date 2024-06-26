package com.example.explory.presentation.screen.quest.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.explory.domain.model.SelectTransport
import com.example.explory.ui.theme.S10_W600
import com.example.explory.ui.theme.S16_W400
import com.example.explory.ui.theme.S20_W600

@Composable
fun TransportDetails(transport: SelectTransport) {
    Text(
        transport.title,
        style = S20_W600,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onBackground
    )
    Spacer(modifier = Modifier.padding(16.dp))
    Text(
        transport.description,
        style = S16_W400,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurface
    )
    Spacer(modifier = Modifier.padding(16.dp))
    Text(
        text = "если система обнаружит, что вы двигаетесь не так, как указано, квест будет отменён",
        style = S10_W600,
        color = MaterialTheme.colorScheme.onError.copy(alpha = 0.6f),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        textAlign = TextAlign.Center,
    )
    Spacer(modifier = Modifier.padding(16.dp))
}