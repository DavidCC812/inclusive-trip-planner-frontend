package com.example.frontend.api

import com.example.frontend.models.Destination
import retrofit2.http.GET

interface DestinationApi {
    @GET("/api/destinations")
    suspend fun getAllDestinations(): List<Destination>
}