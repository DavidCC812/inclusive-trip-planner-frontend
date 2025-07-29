package com.example.frontend.models

import java.util.UUID

data class Review(
    val id: UUID,
    val userId: UUID,
    val itineraryId: UUID,
    val rating: Float,
    val comment: String,
    val createdAt: String,
    val updatedAt: String?
)
