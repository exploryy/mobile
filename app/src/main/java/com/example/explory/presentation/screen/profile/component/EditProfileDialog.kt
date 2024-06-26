package com.example.explory.presentation.screen.profile.component

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toUri
import com.example.explory.data.model.profile.ProfileDto
import com.example.explory.data.model.profile.ProfileRequest
import com.example.explory.presentation.common.Avatar
import com.example.explory.ui.theme.S16_W600
import com.example.explory.ui.theme.S20_W600
import com.example.explory.ui.theme.Value.BasePadding


@Composable
fun EditProfileDialog(
    profile: ProfileDto?,
    onDismiss: () -> Unit,
    onSave: (ProfileRequest) -> Unit
) {
    val name = remember { mutableStateOf(profile?.username ?: "") }
    val email = remember { mutableStateOf(profile?.email ?: "") }
    val password = remember { mutableStateOf("") }
    val avatarUri = remember { mutableStateOf(profile?.avatarUrl ?: "") }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            avatarUri.value = it.toString()
        }
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
                .padding(BasePadding),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "редактирование", style = S20_W600)

                Spacer(modifier = Modifier.height(32.dp))
                EditField(
                    label = "username",
                    value = name.value,
                    onValueChange = { name.value = it }
                )

                Spacer(modifier = Modifier.height(8.dp))
                EditField(
                    label = "email",
                    value = email.value,
                    onValueChange = { email.value = it }
                )

                Spacer(modifier = Modifier.height(8.dp))
                EditField(
                    label = "пароль",
                    value = password.value,
                    onValueChange = { password.value = it },
                    isPassword = true
                )

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        onClick = { launcher.launch("image/*") }
                    ) {
                        Text("выбрать файл", style = S16_W600)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Avatar(
                        image = avatarUri.value,
                        modifier = Modifier.size(40.dp),
                        border = profile?.inventoryDto?.avatarFrames
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onError)
                    ) {
                        Text("отменить", style = S16_W600)
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    TextButton(
                        onClick = {
                            onSave(
                                ProfileRequest(
                                    name.value,
                                    email.value,
                                    password.value,
                                    avatarUri.value.toUri()
                                )
                            )
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onBackground)
                    ) {
                        Text("сохранить", style = S16_W600)
                    }
                }
            }
        }
    }
}
