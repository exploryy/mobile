package com.example.explory.presentation.screen.auth.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.explory.ui.theme.Red
import com.example.explory.ui.theme.S14_W400
import com.example.explory.ui.theme.Transparent
import com.example.explory.ui.theme.Value.BigRound
import com.example.explory.ui.theme.Value.MiddlePadding
import com.example.explory.ui.theme.White

@Composable
fun PasswordTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    transformationState: Boolean,
    onButtonClick: () -> Unit,
    errorText: String? = null,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = {
                    onValueChange(it)
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = MiddlePadding)
                    .height(IntrinsicSize.Min),
                shape = RoundedCornerShape(BigRound),
                visualTransformation = if (transformationState)
                    VisualTransformation.None else PasswordVisualTransformation(),
                label = { Text(text = label) },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            onButtonClick()
                        }
                    ) {
                        Icon(
                            imageVector = if (transformationState) Icons.Default.Visibility
                            else Icons.Default.VisibilityOff,
                            contentDescription = null
                        )
                    }
                },
                isError = errorText != null,
                colors = TextFieldDefaults.colors().copy(
                    focusedContainerColor = Transparent,
                    unfocusedContainerColor = Transparent,
                    focusedTextColor = White,
                    unfocusedTextColor = White,
                    cursorColor = Color.White,
                    errorTextColor = Red,
                    errorPlaceholderColor = Red,
                    focusedLabelColor = Color.White,
                    errorTrailingIconColor = Red,
                    errorLabelColor = Red,
                    unfocusedLabelColor = Color.Gray,
                    errorContainerColor = Red.copy(alpha = 0.1f)
                ),
                textStyle = S14_W400
            )
            errorText?.let {
                Text(
                    text = it,
                    modifier = Modifier
                        .padding(top = MiddlePadding),
                    color = Red,
                    style = S14_W400
                )
            }
        }
    }
}