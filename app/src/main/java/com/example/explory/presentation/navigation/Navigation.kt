package com.example.explory.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.explory.presentation.screen.auth.login.LoginScreen
import com.example.explory.presentation.screen.map.MapScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
    ) {
        composable(Screen.Map.route) {
            MapScreen()
        }
        composable(Screen.Login.route) {
            LoginScreen()
        }
    }
}