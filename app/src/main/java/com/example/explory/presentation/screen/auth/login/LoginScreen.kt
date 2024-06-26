package com.example.explory.presentation.screen.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.explory.ui.theme.S16_W600
import com.example.explory.ui.theme.S24_W600
import com.example.explory.ui.theme.Transparent
import com.example.explory.ui.theme.Value.BasePadding
import com.example.explory.ui.theme.Value.BigRound
import com.example.explory.ui.theme.Value.MoreSpaceBetweenObjects
import com.example.explory.ui.theme.Value.SpaceBetweenObjects
import com.example.explory.ui.theme.White
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onRegistrationClick: () -> Unit,
    onSuccessNavigation: () -> Unit
) {
    val loginState by viewModel.state.collectAsState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(loginState.navigationEvent) {
        loginState.navigationEvent?.let { event ->
            when (event) {
                LoginNavigationEvent.NavigateToMap -> onSuccessNavigation()
                LoginNavigationEvent.NavigateToRegistration -> onRegistrationClick()
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
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    Icons.Rounded.ArrowBackIosNew,
                    contentDescription = "Back",
                )
            }
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Transparent,
            navigationIconContentColor = White
        )
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer(modifier = Modifier.height(256.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.login_to),
                            style = S24_W600,
                            textAlign = TextAlign.Left,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(
                                top = MoreSpaceBetweenObjects, bottom = SpaceBetweenObjects
                            )
                        )

                        OutlinedTextFieldWithLabel(
                            label = stringResource(R.string.login),
                            value = loginState.login,
                            onValueChange = { viewModel.processIntent(LoginIntent.UpdateLogin(it)) },
                            isError = loginState.errorMessage != null,
                            modifier = Modifier
                        )

                        PasswordTextField(
                            label = stringResource(R.string.password),
                            value = loginState.password,
                            onValueChange = { viewModel.processIntent(LoginIntent.UpdatePassword(it)) },
                            transformationState = loginState.isPasswordHide,
                            onButtonClick = { viewModel.processIntent(LoginIntent.UpdatePasswordVisibility) },
                            errorText = loginState.errorMessage,
                            modifier = Modifier
                        )
                        Spacer(modifier = Modifier.height(MoreSpaceBetweenObjects))
                        Button(
                            onClick = {
                                viewModel.processIntent(LoginIntent.Login)
                            },
                            shape = RoundedCornerShape(BigRound),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            enabled = !loginState.isLoading && viewModel.isLoginButtonAvailable(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.onBackground,
                                contentColor = MaterialTheme.colorScheme.background,
                                disabledContentColor = MaterialTheme.colorScheme.background.copy(
                                    alpha = 0.5f
                                ),
                                disabledContainerColor = MaterialTheme.colorScheme.onBackground.copy(
                                    alpha = 0.5f
                                )
                            )
                        ) {
                            Text(
                                text = stringResource(R.string.login_button),
                                style = S16_W600,
                            )
                        }

                        if (loginState.isLoading) {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = BasePadding)
                            )
                            LoadingItem()
                        }

                        AdviceText(
                            baseText = stringResource(R.string.need_register),
                            clickableText = stringResource(R.string.need_register_clickable),
                            onClick = { viewModel.processIntent(LoginIntent.GoToRegistration) },
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(
                            Modifier.windowInsetsBottomHeight(
                                WindowInsets.systemBars
                            )
                        )
                    }
                }
            }
        }
    }
}

