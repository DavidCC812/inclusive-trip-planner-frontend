package com.example.frontend.models

import java.util.UUID

data class ItineraryAccessibility(
    val id: UUID,
    val itineraryId: UUID,
    val featureId: UUID,
    val createdAt: String,
    val updatedAt: String?
)