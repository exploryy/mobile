package com.example.explory.presentation.screen.map.component.review

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.explory.R
import com.example.explory.presentation.screen.map.component.DIALOG_SHAPE
import com.example.explory.presentation.screen.map.component.DIALOG_WIDTH
import com.example.explory.ui.theme.Black
import com.example.explory.ui.theme.DarkGray
import com.example.explory.ui.theme.ExploryTheme
import com.example.explory.ui.theme.Gray
import com.example.explory.ui.theme.S16_W600
import com.example.explory.ui.theme.S20_W600
import com.example.explory.ui.theme.White
import com.gowtham.ratingbar.RatingBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendReviewDialog(
    modifier: Modifier = Modifier,
    onSendReviewClicked: (Int, String, List<Uri>) -> Unit,
    onDismiss: () -> Unit
) {
    val rating = remember { mutableIntStateOf(5) }
    val reviewText = remember { mutableStateOf("") }
    val images = remember { mutableStateOf(emptyList<Uri>()) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            images.value += it
        }
    }


    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .width(DIALOG_WIDTH.dp)
            .wrapContentHeight()
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    BottomSheetDefaults.ContainerColor, shape = RoundedCornerShape(DIALOG_SHAPE.dp)
                )
                .clip(RoundedCornerShape(DIALOG_SHAPE.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Оцените квест", style = S20_W600, color = White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Оцените квест, чтобы помочь другим пользователям выбрать лучший квест",
                style = S16_W600,
                color = Gray,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            RatingBar(value = rating.intValue.toFloat(),
                painterEmpty = painterResource(id = R.drawable.star_empty),
                painterFilled = painterResource(id = R.drawable.star),
                numOfStars = 5,
                size = 25.dp,
                spaceBetween = 4.dp,
                onValueChange = {
                    rating.intValue = it.toInt()
                },
                onRatingChanged = {
                    Log.d("TAG", "onRatingChanged: $it")
                })
            Spacer(modifier = Modifier.height(32.dp))
            CustomBigTextField(placeholder = "Напишите отзыв",
                textFieldValue = reviewText.value,
                onValueChange = { reviewText.value = it })
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = {
                        launcher.launch("image/*")
                    }, modifier = Modifier
                        .size(32.dp)
                        .border(
                            width = 2.dp, color = DarkGray, shape = RoundedCornerShape(8.dp)
                        )
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.picture),
                        contentDescription = null,
                        modifier = Modifier.scale(0.6f),
                        tint = Gray
                    )
                }
                for (image in images.value) {
                    Box(
                        modifier = Modifier.size(48.dp)
                    ) {
                        RemovableImage(image = image.toString(),
                            onRemove = { images.value = images.value.filter { it != image } })
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    onSendReviewClicked(rating.intValue, reviewText.value, images.value)
                    onDismiss()
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Black, containerColor = White
                )
            ) {
                Text(text = "Оценить", style = S16_W600)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@Preview
@Composable
private fun PreviewSendReviewDialog() {
    ExploryTheme {
        SendReviewDialog(
            onSendReviewClicked = { _, _, _ -> },
            onDismiss = { }
        )
    }
}