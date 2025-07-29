package com.example.frontend.models

import java.util.UUID
import java.math.BigDecimal

data class ItineraryStep(
    val id: UUID,
    val itineraryId: UUID,
    val stepIndex: Int,
    val title: String,
    val description: String,
    val latitude: BigDecimal,
    val longitude: BigDecimal,
    val createdAt: String,
    val updatedAt: String?
)