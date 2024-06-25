package com.example.explory.presentation.screen.map.note

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.explory.presentation.screen.map.component.review.RemovableImage
import com.example.explory.ui.theme.Green
import com.example.explory.ui.theme.S14_W400
import com.example.explory.ui.theme.S16_W600
import com.example.explory.ui.theme.S24_W600
import com.example.explory.ui.theme.Value
import com.example.explory.ui.theme.Value.CenterPadding

@Composable
fun CreateNoteScreen(
    onSave: (String, List<Uri>) -> Unit,
    onDismiss: () -> Unit
) {
    val text = remember { mutableStateOf("") }
    val imageUris = remember { mutableStateOf(listOf<Uri>()) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            imageUris.value = uris
        }
    }

    fun removeImage(uri: Uri) {
        imageUris.value = imageUris.value.toMutableList().also { it.remove(uri) }
    }

    Dialog(
        onDismissRequest = {
            onDismiss()
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Value.BasePadding),
            shape = RoundedCornerShape(Value.LittleRound),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Absolute.spacedBy(CenterPadding)
            ) {
                Text(text = "заметка", style = S24_W600)

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = text.value,
                    onValueChange = { text.value = it },
                    label = { Text("текст", style = S16_W600) },
                    textStyle = S14_W400,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                    ),
//                    maxLines = 4,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 98.dp)
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    onClick = { launcher.launch("image/*") }
                ) {
                    Text("выбрать фото", style = S16_W600)
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (imageUris.value.isNotEmpty()) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(imageUris.value) { uri ->
                            RemovableImage(
                                image = uri.toString(),
                                onRemove = { removeImage(uri) },
                                modifier = Modifier.size(100.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }


                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.textButtonColors(contentColor = Color.White)
                    ) {
                        Text("отменить", style = S16_W600)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    TextButton(
                        enabled = text.value.isNotEmpty(),
                        onClick = {
                            onSave(
                                text.value,
                                imageUris.value
                            )
                            onDismiss()
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Green,
                            disabledContentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Text("создать", style = S16_W600)
                    }
                }
            }
        }
    }
}