package com.example.explory.presentation.navigation

sealed class Screen(
    val route: String,
    val title: String
) {
    data object Splash : Screen("splash", "Splash")
    data object Welcome : Screen("welcome", "Welcome")
    data object Login : Screen("login", "Login")
    data object Registration : Screen("registration", "Registration")
    data object Map : Screen("map", "Map")
    data object Error : Screen("error", "Error")
}