package com.example.explory.presentation.navigation

sealed class Screen(
    val route: String,
    val title: String,
    val imageResource: Int? = null,
    val imageResourceFilled: Int? = null
) {
    data object Splash : Screen("splash", "Splash", null)
    data object Welcome : Screen("welcome", "Welcome", null)
    data object Login : Screen("login", "Login", null)
    data object Registration : Screen("registration", "Registration", null)
    data object Map : Screen("map", "Map", null)
    data object Quest : Screen("quest", "Quest", null)
}