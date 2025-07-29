package com.example.frontend.models

import java.util.UUID

data class Itinerary(
    val id: UUID,
    val title: String,
    val description: String,
    val price: Double?,
    val duration: Int?,
    val rating: Float?,
    val destinationName: String,
    val imageUrl: String?,
    val createdAt: String,
    val updatedAt: String?
)
