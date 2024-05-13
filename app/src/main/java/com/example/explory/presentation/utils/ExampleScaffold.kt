package com.example.explory.presentation.utils

import android.app.Activity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExampleScaffold(
    modifier: Modifier = Modifier,
    snackBarHost: @Composable () -> Unit = { },
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val activity = LocalContext.current as? Activity
    val name = remember {
        activity?.title?.toString() ?: "Example"
    }
    val hasParentActivity = remember {
        activity?.parentActivityIntent != null
    }
    Scaffold(
        modifier = modifier,
        snackbarHost = snackBarHost,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        topBar = {
            TopAppBar(title = {
                Text(text = name)
            }, navigationIcon = if (hasParentActivity) {
                {
                    IconButton(onClick = { activity?.finish() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack, contentDescription = "Back"
                        )
                    }
                }
            } else {
                {}
            })
        },
        bottomBar = bottomBar,
        content = content
    )
}