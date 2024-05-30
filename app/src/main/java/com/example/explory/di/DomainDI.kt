package com.example.explory.di

import com.example.explory.domain.usecase.GetPolygonsUseCase
import com.example.explory.domain.usecase.PostLoginUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun domainModule() = module {
    factoryOf(::GetPolygonsUseCase)
    factoryOf(::PostLoginUseCase)
}