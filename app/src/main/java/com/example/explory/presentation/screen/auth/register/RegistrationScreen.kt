package com.example.explory.presentation.screen.auth.register

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.explory.R
import com.example.explory.presentation.screen.auth.component.AdviceText
import com.example.explory.presentation.screen.auth.component.LoadingItem
import com.example.explory.presentation.screen.auth.component.OutlinedTextFieldWithLabel
import com.example.explory.presentation.screen.auth.component.PasswordTextField
import com.example.explory.ui.theme.Black
import com.example.explory.ui.theme.S16_W600
import com.example.explory.ui.theme.S24_W600
import com.example.explory.ui.theme.Value
import com.example.explory.ui.theme.White
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel = koinViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope,
    onBackClick: () -> Unit,
    onSuccessNavigation: () -> Unit,
    onLoginClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val focusManager = LocalFocusManager.current
    val spacerHeight = 128.dp

    LaunchedEffect(state.navigationEvent) {
        state.navigationEvent?.let { event ->
            when (event) {
                NavigationEvent.NavigateBack -> onBackClick()
                NavigationEvent.NavigateToMap -> onSuccessNavigation()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.earth),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
        )

        TopAppBar(title = { Text(text = "") }, navigationIcon = {
            IconButton(onClick = {
                onBackClick()
            }) {
                Icon(Icons.Filled.ArrowBackIosNew, contentDescription = "Back")
            }
        }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent))

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer(modifier = Modifier.height(spacerHeight))

            Column(
                modifier = Modifier
                    .fillMaxSize()
//                    .sharedElement(state = rememberSharedContentState(key = "column"),
//                        animatedVisibilityScope = animatedVisibilityScope,
//                        boundsTransform = { _, _ ->
//                            tween(durationMillis = 500)
//                        })
                , verticalArrangement = Arrangement.Bottom
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
                            style = S24_W600,
                            textAlign = TextAlign.Left,
                            color = White,
                            modifier = Modifier.padding(
                                top = Value.MoreSpaceBetweenObjects,
                                bottom = Value.SpaceBetweenObjects
                            )
                        )

                        OutlinedTextFieldWithLabel(
                            label = stringResource(R.string.email),
                            isError = state.error != null,
                            value = state.email,
                            onValueChange = {
                                viewModel.processIntent(
                                    RegistrationIntent.UpdateEmail(it)
                                )
                            },

                            modifier = Modifier
                        )

                        OutlinedTextFieldWithLabel(
                            label = stringResource(R.string.name),
                            value = state.name,
                            isError = state.error != null,
                            onValueChange = {
                                viewModel.processIntent(
                                    RegistrationIntent.UpdateName(
                                        it
                                    )
                                )
                            },
                            modifier = Modifier
                        )

                        PasswordTextField(
                            label = stringResource(R.string.password),
                            value = state.password,
                            onValueChange = {
                                viewModel.processIntent(
                                    RegistrationIntent.UpdatePassword(
                                        it
                                    )
                                )
                            },
                            errorText = state.error,
                            transformationState = state.isPasswordHide,
                            onButtonClick = { viewModel.processIntent(RegistrationIntent.UpdatePasswordVisibility) },

                            modifier = Modifier
                        )

                        Spacer(modifier = Modifier.height(Value.MoreSpaceBetweenObjects))

                        Button(
                            onClick = {
                                viewModel.processIntent(
                                    RegistrationIntent.Registration(
                                        state.name,
                                        state.email,
                                        state.password
                                    )
                                )
                            },
                            shape = RoundedCornerShape(Value.BigRound),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            enabled = !state.isLoading && viewModel.isContinueButtonAvailable(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White
                            )
                        ) {
                            Text(
                                text = stringResource(R.string.login_button),
                                style = S16_W600,
                                color = Black
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
                            clickableText = stringResource(R.string.need_login_clickable),
                            onClick = { onLoginClick() },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}