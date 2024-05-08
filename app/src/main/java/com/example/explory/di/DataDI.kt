package com.example.explory.di

import com.example.explory.common.Constants
import com.example.explory.data.repository.PolygonRepository
import com.example.explory.data.service.OpenStreetMapService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun dataModule() = module {
    singleOf(::PolygonRepository)

    single<OpenStreetMapService> {
        Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(OpenStreetMapService::class.java)
    }
}