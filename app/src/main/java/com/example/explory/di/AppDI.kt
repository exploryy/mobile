package com.example.explory.di

import com.example.explory.presentation.screen.auth.login.LoginViewModel
import com.example.explory.presentation.screen.auth.register.RegistrationViewModel
import com.example.explory.presentation.screen.map.MapViewModel
import com.example.explory.presentation.screen.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

fun appModule() = module {
    viewModelOf(::MapViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegistrationViewModel)
    viewModelOf(::SplashViewModel)
}