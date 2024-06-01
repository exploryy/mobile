package com.example.explory.di

import com.example.explory.common.Constants
import com.example.explory.data.repository.PolygonRepository
import com.example.explory.data.service.OpenStreetMapService
import com.example.explory.domain.websocket.LocationProvider
import com.example.explory.domain.websocket.LocationWebSocketClient
import com.example.explory.domain.websocket.MapLocationProvider
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
    single<LocationProvider> { MapLocationProvider(get()) }
    single {
        LocationWebSocketClient(
            url = "ws://158.160.85.50:8080/ws/location",
            token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJKTzRYZE9LWl9MNWtfRmE3LUNNTHk0d3QtRXVOS2puMzBaQWtzQ0Y1TUhjIn0.eyJleHAiOjE3MTcyMjE5NzMsImlhdCI6MTcxNzIyMDc3MywianRpIjoiNzkxZmRjZTAtMDdkYi00MjM0LTkxNjQtNzU4ZmNlZTMzOTE3IiwiaXNzIjoiaHR0cDovLzE1OC4xNjAuNjkuMTYwOjgwODIvcmVhbG1zL2hpdHMtcHJvamVjdCIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI5YmY5ZDQ4YS1kMjc3LTQ2YzMtYTVjMS00ZDFjMjI0NTQwMzYiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJ1c2Vycy1hcHAiLCJzZXNzaW9uX3N0YXRlIjoiNDNhYjcyMzktYTViYS00NzhjLWE3ODctZGRlYWQzZDZjYTQyIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vMTU4LjE2MC42OS4xNjA6ODA4MCJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiUk9MRV9VU0VSIiwib2ZmbGluZV9hY2Nlc3MiLCJkZWZhdWx0LXJvbGVzLWhpdHMtcHJvamVjdCIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJlbWFpbCBwcm9maWxlIiwic2lkIjoiNDNhYjcyMzktYTViYS00NzhjLWE3ODctZGRlYWQzZDZjYTQyIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJzcHJpbmdfc2VjX3JvbGVzIjpbIlJPTEVfVVNFUiIsIm9mZmxpbmVfYWNjZXNzIiwiZGVmYXVsdC1yb2xlcy1oaXRzLXByb2plY3QiLCJ1bWFfYXV0aG9yaXphdGlvbiJdLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJnaHVibWFuMSIsImVtYWlsIjoiZ2h1Ym1hbjFAZ21haWwuY29tIn0.XRzvt6aZ6Ske6NBUvXEsgSEcIKWTNZRrFIYTWzJchuhbKHdYpG9T5WK__dRutRWTsYbwXtayVFI4jM5YwlhwmucQ8eYygiJxNtfKqgjsGQpTorKd1mjXUFum3VgbJ9l4aXZVLIW4uVMYP-HuButN7gndIU6HqF-l1HSIkiwkyiH63GsXL-cHFIiCFYz8fL6Q76IAO20Q1KFOSavFFrSW5Zq-RbQjK9in9fChJ6TDGiGX5vI7xnXxZLoT4ZXma_gmUiYL-BFfnMsxOuyXoeho8CmdxsqFLuJfj49hMrqZmnUeO2VZwyrPdliuxN6Sp0X0DpuGJexlPoYUAugUgM2K1A"
        )
    }
}