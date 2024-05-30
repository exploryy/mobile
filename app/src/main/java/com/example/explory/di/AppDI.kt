package com.example.explory.di

import com.example.explory.presentation.screen.map.MapViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

fun appModule() = module {
    viewModelOf(::MapViewModel)
}