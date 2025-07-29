package com.example.frontend.api

import com.example.frontend.models.ItineraryStep
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.UUID

interface ItineraryStepApi {

    @GET("/api/itinerary-steps/by-itinerary/{itineraryId}")
    suspend fun getStepsForItinerary(@Path("itineraryId") itineraryId: UUID): List<ItineraryStep>
}
