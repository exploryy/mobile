package com.example.explory.presentation.screen.quest.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import com.example.explory.ui.theme.S14_W600


@Composable
fun InfoBox(
    modifier: Modifier = Modifier,
    text: String,
    containerColor: Color = MaterialTheme.colorScheme.secondary,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    isCopyingEnabled: Boolean = false
) {
    val clipboardManager = LocalClipboardManager.current
    val annotatedString = buildAnnotatedString {
        append(text)
    }
    Box(
        modifier = modifier
            .background(containerColor, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .clip(RoundedCornerShape(8.dp))

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                annotatedString,
                style = S14_W600,
                color = textColor
            )
            if (isCopyingEnabled) {
                Spacer(modifier = Modifier.width(4.dp))
                IconButton(
                    onClick = { clipboardManager.setText(annotatedString) },
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ContentCopy,
                        contentDescription = "Copy",
                        tint = textColor,
                        modifier = Modifier.size(16.dp)
                    )
                }

            }
        }

    }
}