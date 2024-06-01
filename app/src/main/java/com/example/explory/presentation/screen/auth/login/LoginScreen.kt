package com.example.explory.presentation.screen.auth.login

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.explory.R
import com.example.explory.presentation.screen.auth.component.AdviceText
import com.example.explory.presentation.screen.auth.component.LoadingItem
import com.example.explory.presentation.screen.auth.component.OutlinedTextFieldWithLabel
import com.example.explory.presentation.screen.auth.component.PasswordTextField
import com.example.explory.ui.theme.Value.BasePadding
import com.example.explory.ui.theme.Value.BigRound
import com.example.explory.ui.theme.Value.MoreSpaceBetweenObjects
import com.example.explory.ui.theme.Value.SpaceBetweenObjects
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope,
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
        TopAppBar(title = { Text(text = "") }, navigationIcon = {
            IconButton(onClick = { onBackClick() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
        )

        Spacer(modifier = Modifier.height(128.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .sharedElement(state = rememberSharedContentState(key = "column"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    boundsTransform = { _, _ ->
                        tween(durationMillis = 500)
                    }), verticalArrangement = Arrangement.Bottom
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
                        text = stringResource(R.string.login_to),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Left,
                        modifier = Modifier.padding(
                            top = MoreSpaceBetweenObjects, bottom = SpaceBetweenObjects
                        )
                    )

                    OutlinedTextFieldWithLabel(
                        label = stringResource(R.string.login),
                        value = loginState.login,
                        onValueChange = { viewModel.processIntent(LoginIntent.UpdateLogin(it)) },
                        error = loginState.errorMessage,
                        colors = OutlinedTextFieldDefaults.colors(
                            errorContainerColor = Color.Red.copy(alpha = 0.1f),
                            cursorColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.Gray,
                            errorBorderColor = Color.Red,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.Gray,
                        ),
                        modifier = Modifier
                    )

                    PasswordTextField(
                        label = stringResource(R.string.password),
                        value = loginState.password,
                        onValueChange = { viewModel.processIntent(LoginIntent.UpdatePassword(it)) },
                        transformationState = loginState.isPasswordHide,
                        onButtonClick = { viewModel.processIntent(LoginIntent.UpdatePasswordVisibility) },
                        errorText = loginState.errorMessage,
                        colors = OutlinedTextFieldDefaults.colors(
                            errorContainerColor = Color.Red.copy(alpha = 0.1f),
                            cursorColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.Gray,
                            errorBorderColor = Color.Red,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.Gray,
                        ),
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
                            .height(IntrinsicSize.Min),
                        enabled = !loginState.isLoading && viewModel.isLoginButtonAvailable(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.login_button), style = TextStyle(
                                fontSize = 15.sp, fontWeight = FontWeight.W600, color = Color.Black
                            )
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
                }
            }
        }
    }
}
