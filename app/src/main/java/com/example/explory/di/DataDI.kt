package com.example.explory.di

import com.example.explory.common.Constants
import com.example.explory.data.network.interceptor.AuthInterceptor
import com.example.explory.data.repository.AuthRepository
import com.example.explory.data.repository.PolygonRepository
import com.example.explory.data.service.ApiService
import com.example.explory.data.service.AuthService
import com.example.explory.data.service.OpenStreetMapService
import com.example.explory.data.storage.LocalStorage
import com.example.explory.domain.websocket.LocationProvider
import com.example.explory.domain.websocket.LocationWebSocketClient
import com.example.explory.domain.websocket.MapLocationProvider
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun dataModule() = module {
    singleOf(::PolygonRepository)
    singleOf(::AuthRepository)
    singleOf(::LocalStorage)
    single<OpenStreetMapService> {
        Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(OpenStreetMapService::class.java)
    }
    single<Interceptor> { AuthInterceptor(get(), get()) }

    single<OkHttpClient> {
        OkHttpClient.Builder().addInterceptor(get<Interceptor>())
            .connectTimeout(20L, TimeUnit.SECONDS)
            .writeTimeout(20L, TimeUnit.SECONDS).readTimeout(20L, TimeUnit.SECONDS).build()
    }

    single<AuthService> {
        Retrofit.Builder().baseUrl(Constants.AUTH_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(AuthService::class.java)
    }

    single<ApiService> {
        Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ApiService::class.java)
    }

    single<LocationProvider> { MapLocationProvider(get()) }
    single {
        LocationWebSocketClient(
            url = "ws://158.160.69.160:8080/ws/location",
            get()
        )
    }
}