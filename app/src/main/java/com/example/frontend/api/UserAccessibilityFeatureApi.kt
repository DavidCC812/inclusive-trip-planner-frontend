package com.example.frontend.api

import com.example.frontend.models.UserAccessibilityFeature
import com.example.frontend.models.UserAccessibilityFeatureRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserAccessibilityFeatureApi {
    @GET("/api/user-accessibility-features")
    suspend fun getAllUserAccessibilityFeatures(): List<UserAccessibilityFeature>

    @POST("/api/user-accessibility-features")
    suspend fun createUserAccessibilityFeature(
        @Body request: UserAccessibilityFeatureRequest
    ): UserAccessibilityFeature
}
