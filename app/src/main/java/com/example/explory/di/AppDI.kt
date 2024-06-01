package com.example.explory.di

import com.example.explory.presentation.screen.auth.login.LoginViewModel
import com.example.explory.presentation.screen.auth.register.RegistrationViewModel
import com.example.explory.presentation.screen.map.MapViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

fun appModule() = module {
    viewModel {
        MapViewModel(get(), get(), get())
    }
    viewModel {
        LoginViewModel(get(), get())
    }
    viewModel {
        RegistrationViewModel(get(), get())
    }
}