package com.example.explory.presentation.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.explory.presentation.screen.auth.login.LoginScreen
import com.example.explory.presentation.screen.auth.onboarding.OnBoardingScreen
import com.example.explory.presentation.screen.auth.register.RegistrationScreen
import com.example.explory.presentation.screen.map.MapScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = Screen.Welcome.route,
        ) {
            composable(Screen.Welcome.route) {
                OnBoardingScreen(
                    animatedVisibilityScope = this,
                    onClickNavigation = {
                        navController.navigate(Screen.Login.route)
                    }
                )
            }
            composable(Screen.Map.route) {
                MapScreen()
            }
            composable(Screen.Login.route) {
                LoginScreen(
                    animatedVisibilityScope = this,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onRegistrationClick = {
                        navController.navigate(Screen.Registration.route) {
                            popUpTo(Screen.Welcome.route)
                        }
                    },
                    onSuccessNavigation = {
                        navController.navigate(Screen.Map.route) {
                            popUpTo(Screen.Login.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable(Screen.Registration.route) {
                RegistrationScreen(
                    animatedVisibilityScope = this,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onLoginClick = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Welcome.route)
                        }
                    }
                )
            }
        }
    }
}