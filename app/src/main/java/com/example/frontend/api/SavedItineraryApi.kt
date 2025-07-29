package com.example.frontend.api

import com.example.frontend.models.SavedItinerary
import com.example.frontend.models.SavedItineraryRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.UUID

interface SavedItineraryApi {

    @GET("/api/saved-itineraries/user/{userId}")
    suspend fun getByUserId(@Path("userId") userId: UUID): List<SavedItinerary>

    @POST("/api/saved-itineraries")
    suspend fun saveItinerary(@Body request: SavedItineraryRequest): SavedItinerary

    @DELETE("/api/saved-itineraries/{id}")
    suspend fun deleteItinerary(@Path("id") id: UUID)
}
