package com.example.frontend.api

import com.example.frontend.models.User
import com.example.frontend.models.UserRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApi {

    @POST("api/users")
    suspend fun createUser(@Body request: UserRequest): User

    @GET("api/users/{id}")
    suspend fun getUserById(@Path("id") id: String): User

    @GET("api/users/email/{email}")
    suspend fun getUserByEmail(@Path("email") email: String): User

    @GET("api/users/phone/{phone}")
    suspend fun getUserByPhone(@Path("phone") phone: String): User
}

