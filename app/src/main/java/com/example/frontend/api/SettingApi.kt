package com.example.frontend.api

import com.example.frontend.models.Setting
import retrofit2.http.GET

interface SettingApi {
    @GET("/api/settings")
    suspend fun getAllSettings(): List<Setting>
}
