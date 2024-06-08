package com.example.explory.presentation.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.explory.presentation.screen.auth.login.LoginScreen
import com.example.explory.presentation.screen.auth.onboarding.OnBoardingScreen
import com.example.explory.presentation.screen.auth.register.RegistrationScreen
import com.example.explory.presentation.screen.map.MapScreen
import com.example.explory.presentation.screen.quest.QuestScreen
import com.example.explory.presentation.screen.splash.SplashScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(onNavigateToMap = {
                    navController.navigate(Screen.Map.route) {
                        popUpTo(Screen.Splash.route)
                    }
                }, onNavigateToWelcome = {
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.Splash.route)
                    }
                })
            }
            composable(Screen.Welcome.route) {
                OnBoardingScreen(animatedVisibilityScope = this, onClickNavigation = {
                    navController.navigate(Screen.Login.route)
                })
            }
            composable(Screen.Map.route) {
                MapScreen(onLogout = {
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.Map.route) {
                            inclusive = true
                        }
                    }
                }, onNavigateToQuest = { questId, questType ->
                    navController.navigate("${Screen.Quest.route}/$questId/$questType")
                })
            }
            composable(Screen.Login.route) {
                LoginScreen(animatedVisibilityScope = this, onBackClick = {
                    navController.popBackStack()
                }, onRegistrationClick = {
                    navController.navigate(Screen.Registration.route) {
                        popUpTo(Screen.Welcome.route)
                    }
                }, onSuccessNavigation = {
                    navController.navigate(Screen.Map.route) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
                })
            }
            composable(Screen.Registration.route) {
                RegistrationScreen(animatedVisibilityScope = this, onBackClick = {
                    navController.popBackStack()
                }, onLoginClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Welcome.route)
                    }
                })
            }
            composable(Screen.Quest.route + "/{questId}/{questType}",
                arguments = listOf(navArgument("questId") {
                    type = NavType.StringType
                }, navArgument("questType") {
                    type = NavType.StringType
                })
            ) {
                val questId = it.arguments?.getString("questId") ?: ""
                val questType = it.arguments?.getString("questType") ?: ""
                QuestScreen(questId = questId, questType = questType, onNavigateBack = {
                    navController.popBackStack()
                })
            }
        }
    }
}