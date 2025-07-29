package com.example.frontend.models

import java.util.UUID

data class Destination(
    val id: UUID,
    val name: String,
    val type: String,
    val available: Boolean,
    val countryId: UUID,
    val countryName: String,
    val createdAt: String,
    val updatedAt: String?
)
