package com.example.explory.data.network.interceptor

import com.example.explory.common.Constants.Companion.AUTHORIZATION_HEADER
import com.example.explory.data.model.TokenType
import com.example.explory.data.service.AuthService
import com.example.explory.data.storage.LocalStorage
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val localStorage: LocalStorage, private val authService: AuthService
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val builder = request.newBuilder()
        if (request.header(AUTHORIZATION_HEADER) == null) {
            var accessToken = localStorage.fetchToken(TokenType.ACCESS)
            if (accessToken != null) {
                if (localStorage.isAccessTokenExpired() && !request.url.encodedPath
                        .contains("openid-connect/token")
                ) {
                    val refreshToken = localStorage.fetchToken(TokenType.REFRESH)
                    if (refreshToken != null) {
                        val newTokens = runBlocking {
                            authService.refreshToken(refreshToken)
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
