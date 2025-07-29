package com.example.frontend.models

import java.util.UUID

data class UserCountryAccessRequest(
    val userId: UUID,
    val countryId: UUID
)
