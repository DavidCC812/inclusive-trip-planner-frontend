package com.example.frontend.models

import java.util.UUID

data class SavedItinerary(
    val id: UUID,
    val userId: UUID,
    val itineraryId: UUID,
    val savedAt: String,
    val createdAt: String,
    val updatedAt: String?
)