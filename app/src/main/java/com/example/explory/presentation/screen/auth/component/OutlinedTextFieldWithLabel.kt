package com.example.explory.presentation.screen.auth.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.explory.ui.theme.Red
import com.example.explory.ui.theme.S14_W400
import com.example.explory.ui.theme.Value.BigRound
import com.example.explory.ui.theme.Value.MiddlePadding
import com.example.explory.ui.theme.White

@Composable
fun OutlinedTextFieldWithLabel(
    label: String,
    value: String,
    onValueChange: ((String) -> Unit)? = null,
    isError: Boolean = false,
    error: String? = null,
    modifier: Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = {
                    if (onValueChange != null) {
                        onValueChange(it)
                    }
                },
                singleLine = true,
                label = { Text(text = label) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = MiddlePadding),
                shape = RoundedCornerShape(BigRound),
                isError = isError,
                colors = TextFieldDefaults.colors().copy(
                    focusedTextColor = White,
                    unfocusedTextColor = White,
                    cursorColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.Gray,
                    errorTextColor = Red,
                    errorLabelColor = Red,
                    errorPlaceholderColor = Red,
                    errorContainerColor = Red.copy(alpha = 0.1f)
                ),
                textStyle = S14_W400
            )

            error?.let {
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