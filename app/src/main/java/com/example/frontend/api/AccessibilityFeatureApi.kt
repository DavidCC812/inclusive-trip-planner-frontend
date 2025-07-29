package com.example.frontend.api

import com.example.frontend.models.AccessibilityFeature
import retrofit2.http.GET

interface AccessibilityFeatureApi {
    @GET("/api/accessibility-features")
    suspend fun getAccessibilityFeatures(): List<AccessibilityFeature>
}
