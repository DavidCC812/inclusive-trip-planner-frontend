package com.example.frontend.api

import com.example.frontend.models.LoginRequest
import com.example.frontend.models.AuthResponse
import com.example.frontend.models.FirebaseSignInRequest
import retrofit2.http.Body
import retrofit2.Response
import retrofit2.http.POST

interface AuthApi {

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("/api/auth/google")
    suspend fun loginWithFirebase(
        @Body request: FirebaseSignInRequest
    ): Response<AuthResponse>

    @POST("/api/auth/facebook")
    suspend fun loginWithFacebook(
        @Body request: FirebaseSignInRequest
    ): Response<AuthResponse>

}
