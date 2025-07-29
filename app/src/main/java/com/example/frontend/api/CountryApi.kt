package com.example.frontend.api

import com.example.frontend.models.Country
import retrofit2.http.GET

interface CountryApi {
    @GET("/api/countries")
    suspend fun getAllCountries(): List<Country>
}
