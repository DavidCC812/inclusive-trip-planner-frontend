package com.example.frontend.models

import java.util.UUID

data class AccessibilityFeature(
    val id: UUID,
    val name: String,
    val createdAt: String,
    val updatedAt: String?
)
