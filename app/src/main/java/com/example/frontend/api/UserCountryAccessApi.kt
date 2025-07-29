package com.example.frontend.api

import com.example.frontend.models.UserCountryAccess
import com.example.frontend.models.UserCountryAccessRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface UserCountryAccessApi {
    @POST("/api/user-country-access")
    suspend fun createUserCountryAccess(@Body request: UserCountryAccessRequest): UserCountryAccess
}
