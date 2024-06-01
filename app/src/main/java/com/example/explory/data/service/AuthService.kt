package com.example.explory.data.service

import com.example.explory.common.Constants.Companion.CLIENT_ID
import com.example.explory.common.Constants.Companion.CLIENT_SECRET
import com.example.explory.common.Constants.Companion.GRANT_TYPE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthService {
    @FormUrlEncoded
    @POST("realms/hits-project/protocol/openid-connect/token")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("client_id") clientId: String = CLIENT_ID,
        @Field("grant_type") grantType: String = GRANT_TYPE,
        @Field("client_secret") clientSecret: String = CLIENT_SECRET
    ): AuthTokenResponse

    @FormUrlEncoded
    @POST("realms/hits-project/protocol/openid-connect/token")
    suspend fun refreshToken(
        @Field("refresh_token") refreshToken: String,
        @Field("client_id") clientId: String = CLIENT_ID,
        @Field("grant_type") grantType: String = "refresh_token",
        @Field("client_secret") clientSecret: String = CLIENT_SECRET
    ): AuthTokenResponse
}

data class AuthTokenResponse(
    val access_token: String,
    val expires_in: Int,
    val refresh_expires_in: Int,
    val refresh_token: String,
    val token_type: String,
    val not_before_policy: Int,
    val session_state: String,
    val scope: String
)
