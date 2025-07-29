package com.example.frontend.models

import java.util.UUID

data class UserSelectedDestination(
    val id: UUID,
    val userId: UUID,
    val destinationId: UUID,
    val createdAt: String,
    val updatedAt: String?
)