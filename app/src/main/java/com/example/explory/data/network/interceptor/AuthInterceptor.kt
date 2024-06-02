package com.example.explory.data.network.interceptor

import android.util.Log
import com.example.explory.common.Constants.Companion.AUTHORIZATION_HEADER
import com.example.explory.data.model.token.TokenType
import com.example.explory.data.storage.LocalStorage
import com.example.explory.domain.usecase.RefreshTokenUseCase
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val localStorage: LocalStorage, private val refreshTokenUseCase: RefreshTokenUseCase
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d("AuthInterceptor", "Intercepting request")
        var accessToken: String?
        var request = chain.request()
        val builder = request.newBuilder()
        if (request.header(AUTHORIZATION_HEADER) == null) {
            accessToken = localStorage.fetchToken(TokenType.ACCESS)
            if (accessToken != null) {
                Log.d("AuthInterceptor", "Access token is not null")
                if (localStorage.isAccessTokenExpired() && !request.url.encodedPath
                        .contains("openid-connect/token")
                ) {
                    val refreshToken = localStorage.fetchToken(TokenType.REFRESH)
                    if (refreshToken != null) {
                        try {
                            val newTokens = runBlocking {
                                refreshTokenUseCase.execute(refreshToken)
                            }
                            accessToken = newTokens.access_token
                            localStorage.saveToken(
                                accessToken, newTokens.expires_in, TokenType.ACCESS
                            )
                            localStorage.saveToken(
                                newTokens.refresh_token,
                                newTokens.refresh_expires_in,
                                TokenType.REFRESH
                            )
                        } catch (e: Exception) {
                            Log.e("AuthInterceptor", "Error refreshing token",e)
                        }
                    }
                }
                accessToken.let {
                    builder.addHeader(AUTHORIZATION_HEADER, "Bearer $it")
                }
            }
        }

        request = builder.build()
        return chain.proceed(request)
    }

}
