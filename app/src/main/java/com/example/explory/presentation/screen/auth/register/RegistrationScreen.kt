package com.example.explory.presentation.screen.auth.register

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.explory.R
import com.example.explory.presentation.screen.auth.components.AdviceText
import com.example.explory.presentation.screen.auth.components.LoadingItem
import com.example.explory.presentation.screen.auth.components.OutlinedTextFieldWithLabel
import com.example.explory.presentation.screen.auth.components.PasswordTextField
import com.example.explory.presentation.screen.auth.login.LoginIntent
import com.example.explory.ui.theme.Value
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TopAppBar(
            title = { Text(text = "") },
            navigationIcon = {
                IconButton(onClick = {  }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )

        Spacer(modifier = Modifier.height(64.dp))

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                    .background(Color.Black)
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.to_register),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Left,
                        modifier = Modifier.padding(top = Value.MoreSpaceBetweenObjects, bottom = Value.SpaceBetweenObjects)
                    )

                    OutlinedTextFieldWithLabel(
                        label = stringResource(R.string.email),
                        value = state.email,
                        onValueChange = { viewModel.processIntent(RegistrationIntent.UpdateEmail(it)) },
                        error = state.isErrorEmailText,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.Gray,
                            cursorColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.Gray,
                            errorBorderColor = Color.Red,
                            errorContainerColor = Color.Red.copy(alpha = 0.1f)
                        ),
                        modifier = Modifier
                    )

                    OutlinedTextFieldWithLabel(
                        label = stringResource(R.string.name),
                        value = state.name,
                        onValueChange = { viewModel.processIntent(RegistrationIntent.UpdateName(it)) },
                        error = state.isErrorNameText,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.Gray,
                            cursorColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.Gray,
                            errorBorderColor = Color.Red,
                            errorContainerColor = Color.Red.copy(alpha = 0.1f)
                        ),
                        modifier = Modifier
                    )

                    PasswordTextField(
                        label = stringResource(R.string.password),
                        value = state.password,
                        onValueChange = { viewModel.processIntent(RegistrationIntent.UpdatePassword(it)) },
                        transformationState = state.isPasswordHide,
                        onButtonClick = { viewModel.processIntent(RegistrationIntent.UpdatePasswordVisibility) },
                        errorText = state.isErrorPasswordText,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.Gray,
                            cursorColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.Gray,
                            errorBorderColor = Color.Red,
                            errorContainerColor = Color.Red.copy(alpha = 0.1f)
                        ),
                        modifier = Modifier
                    )

                    Spacer(modifier = Modifier.height(Value.MoreSpaceBetweenObjects))

                    Button(
                        onClick = {
                            viewModel.processIntent(RegistrationIntent.Registration(state))
                        },
                        shape = RoundedCornerShape(Value.BigRound),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min),
                        enabled = !state.isLoading && viewModel.isContinueButtonAvailable(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.login_button),
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.W600,
                                color = Color.Black
                            )
                        )
                    }

                    if (state.isLoading) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = Value.BasePadding)
                        )
                        LoadingItem()
                    }

                    AdviceText(
                        baseText = stringResource(R.string.need_login),
                        clickableText = stringResource(R.string.need_login),
                        onClick = { viewModel.processIntent(RegistrationIntent.GoToLogin) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}