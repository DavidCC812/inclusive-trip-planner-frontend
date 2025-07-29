package com.example.frontend.models

import java.util.UUID

data class ReviewRequest(
    val userId: UUID,
    val itineraryId: UUID,
    val rating: Float,
    val comment: String
)
