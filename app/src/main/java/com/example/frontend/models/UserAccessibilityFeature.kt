package com.example.frontend.models

import java.util.UUID

data class UserAccessibilityFeature(
    val id: UUID,
    val userId: UUID,
    val featureId: UUID,
    val createdAt: String,
    val updatedAt: String?
)