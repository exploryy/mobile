package com.example.explory.presentation.screen.auth.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import com.example.explory.ui.theme.Value.BasePadding
import com.example.explory.ui.theme.spanStyleAccent
import com.example.explory.ui.theme.spanStyleGray

@Composable
fun AdviceText(
    baseText: String,
    clickableText: String,
    onClick: () -> Unit,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.BottomCenter)
            .padding(BasePadding),
    ) {
        val highlightedText = buildAnnotatedString {
            withStyle(style = spanStyleGray) {
                append("$baseText ")
            }


            withLink(
                link = LinkAnnotation.Clickable(
                    tag = "clickable",
                    linkInteractionListener = {
                        onClick()
                    }
                ),
                block = {
                    withStyle(style = spanStyleAccent) {
                        append(clickableText)
                    }
                }
            )
        }

        Text(text = highlightedText)
    }
}