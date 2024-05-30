package com.example.explory.presentation.screen.auth.login

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.explory.ui.theme.Value.BasePadding
import com.example.explory.ui.theme.Value.BigRound
import com.example.explory.ui.theme.Value.ButtonHeight
import com.example.explory.ui.theme.Value.MoreSpaceBetweenObjects
import com.example.explory.ui.theme.Value.SpaceBetweenObjects

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
   val loginState by viewModel.state.collectAsState()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.login_to),
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.W700),
            textAlign = TextAlign.Left,
            modifier = Modifier.padding(top = MoreSpaceBetweenObjects, bottom = SpaceBetweenObjects)
        )

        OutlinedTextFieldWithLabel(
            label = stringResource(R.string.login),
            value = loginState.login,
            onValueChange = { viewModel.processIntent(LoginIntent.UpdateLogin(it)) },
            error = loginState.isErrorText,
            modifier = Modifier
        )

        PasswordTextField(
            label = stringResource(R.string.password),
            value = loginState.password,
            onValueChange = { viewModel.processIntent(LoginIntent.UpdatePassword(it)) },
            transformationState = loginState.isPasswordHide,
            onButtonClick = { viewModel.processIntent(LoginIntent.UpdatePasswordVisibility) },
            errorText = loginState.isErrorText,
            modifier = Modifier.padding(top = SpaceBetweenObjects)
        )
        Spacer(modifier = Modifier.height(MoreSpaceBetweenObjects))
        Button(
            onClick = {
                viewModel.processIntent(LoginIntent.Login)
            },
            shape = RoundedCornerShape(BigRound),
            modifier = Modifier
                .fillMaxWidth()
                .height(ButtonHeight),
            enabled = !loginState.isLoading && viewModel.isLoginButtonAvailable()
        ) {
            Text(
                text = stringResource(R.string.login_button),
                style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.W600)
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