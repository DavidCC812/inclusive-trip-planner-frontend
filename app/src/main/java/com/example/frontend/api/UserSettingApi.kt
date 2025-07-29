package com.example.frontend.api

import com.example.frontend.models.UserSetting
import com.example.frontend.models.UserSettingRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserSettingApi {
    @GET("/api/user-settings")
    suspend fun getAllUserSettings(): List<UserSetting>

    @POST("/api/user-settings")
    suspend fun postUserSetting(@Body request: UserSettingRequest): UserSetting

    @PUT("/api/user-settings/{id}")
    suspend fun updateUserSetting(
        @Path("id") id: String,
        @Body request: UserSettingRequest
    ): UserSetting


}


