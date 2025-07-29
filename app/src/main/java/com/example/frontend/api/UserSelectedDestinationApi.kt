package com.example.frontend.api

import com.example.frontend.models.UserSelectedDestination
import com.example.frontend.models.UserSelectedDestinationRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface UserSelectedDestinationApi {
    @POST("/api/user-selected-destinations")
    suspend fun createUserSelectedDestination(
        @Body request: UserSelectedDestinationRequest
    ): UserSelectedDestination
}
