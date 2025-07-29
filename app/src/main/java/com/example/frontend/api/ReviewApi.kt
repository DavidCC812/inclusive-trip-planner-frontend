package com.example.frontend.api

import com.example.frontend.models.Review
import com.example.frontend.models.ReviewRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.UUID

interface ReviewApi {

    @GET("/api/reviews")
    suspend fun getAllReviews(): List<Review>

    @GET("/api/reviews/{id}")
    suspend fun getReviewById(@Path("id") id: UUID): Review

    @POST("/api/reviews")
    suspend fun postReview(@Body reviewRequest: ReviewRequest): Review
}
