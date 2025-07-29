package com.example.frontend.models

import java.util.UUID

data class SavedItineraryRequest(
    val userId: UUID,
    val itineraryId: UUID
)
