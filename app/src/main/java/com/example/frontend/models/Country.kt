package com.example.frontend.models

import java.util.UUID

data class Country(
    val id: UUID,
    val name: String,
    val available: Boolean,
    val createdAt: String,
    val updatedAt: String?
)
