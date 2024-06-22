package com.example.explory.di

import com.example.explory.common.Constants
import com.example.explory.common.Constants.Companion.BASE_URL_WEBSOCKET
import com.example.explory.data.AuthInterceptor
import com.example.explory.data.repository.AuthRepository
import com.example.explory.data.repository.BattlePassRepository
import com.example.explory.data.repository.BuffRepository
import com.example.explory.data.repository.CoinsRepository
import com.example.explory.data.repository.FriendRepository
import com.example.explory.data.repository.InventoryRepository
import com.example.explory.data.repository.NoteRepository
import com.example.explory.data.repository.PolygonRepository
import com.example.explory.data.repository.ProfileRepository
import com.example.explory.data.repository.QuestRepository
import com.example.explory.data.repository.RegisterRepository
import com.example.explory.data.repository.ShopRepository
import com.example.explory.data.repository.StatisticRepository
import com.example.explory.data.service.ApiService
import com.example.explory.data.service.AuthService
import com.example.explory.data.storage.LocalStorage
import com.example.explory.data.storage.ThemePreferenceManager
import com.example.explory.data.websocket.EventWebSocketClient
import com.example.explory.data.websocket.FriendsLocationWebSocketClient
import com.example.explory.data.websocket.LocationProvider
import com.example.explory.data.websocket.LocationTracker
import com.example.explory.data.websocket.LocationWebSocketClient
import com.example.explory.data.websocket.MapLocationProvider
import com.example.explory.foreground.DefaultLocationClient
import com.example.explory.foreground.LocationClient
import com.google.android.gms.location.LocationServices
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun dataModule() = module {
    singleOf(::PolygonRepository)
    singleOf(::ProfileRepository)
    singleOf(::FriendRepository)
    singleOf(::AuthRepository)
    singleOf(::CoinsRepository)
    singleOf(::QuestRepository)
    singleOf(::RegisterRepository)
    singleOf(::ShopRepository)
    singleOf(::InventoryRepository)
    singleOf(::BattlePassRepository)
    singleOf(::LocalStorage)
    singleOf(::ThemePreferenceManager)
    singleOf(::LocationTracker)
    singleOf(::NoteRepository)
    singleOf(::StatisticRepository)
    singleOf(::BuffRepository)

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

    single<LocationClient> {
        DefaultLocationClient(
            get(),
            LocationServices.getFusedLocationProviderClient(androidContext())
        )
    }

    single<LocationProvider> {
        MapLocationProvider(get())
    }

    single {
        LocationWebSocketClient(
            url = "ws://158.160.69.160:8080/ws/location",
            get(),
        )
    }

    single {
        FriendsLocationWebSocketClient(
            baseUrl = "ws://158.160.69.160:8080/ws/friendPosition",
            get()
        )
    }

    single {
        EventWebSocketClient(
            url = "$BASE_URL_WEBSOCKET/event",
            get()
        )
    }
}