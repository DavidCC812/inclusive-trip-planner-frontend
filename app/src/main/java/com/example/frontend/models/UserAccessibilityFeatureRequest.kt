package com.example.frontend.models

import java.util.UUID

data class UserAccessibilityFeatureRequest(
    val userId: UUID,
    val featureId: UUID
)
