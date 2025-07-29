package com.example.frontend.api

import com.example.frontend.models.Itinerary
import retrofit2.http.GET

interface ItineraryApi {
    @GET("/api/itineraries")
    suspend fun getAllItineraries(): List<Itinerary>
}
