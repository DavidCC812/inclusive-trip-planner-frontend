package com.example.frontend.models

import java.util.UUID

data class UserSelectedDestinationRequest(
    val userId: UUID,
    val destinationId: UUID
)
