package com.example.explory.di

import com.example.explory.domain.usecase.CheckUserTokenUseCase
import com.example.explory.domain.usecase.GetPolygonsUseCase
import com.example.explory.domain.usecase.PostLoginUseCase
import com.example.explory.domain.usecase.PostRegistrationUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun domainModule() = module {
    factoryOf(::GetPolygonsUseCase)
    factoryOf(::PostLoginUseCase)
    factoryOf(::PostRegistrationUseCase)
    factoryOf(::CheckUserTokenUseCase)
}