package com.example.frontend.models

import java.util.UUID

data class UserCountryAccess(
    val id: UUID,
    val userId: UUID,
    val countryId: UUID,
    val countryName: String,
    val createdAt: String,
    val updatedAt: String?
)
