package com.example.frontend.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.frontend.models.UserAccessibilityFeature
import java.util.UUID

class UserAccessibilityFeatureViewModel : ViewModel() {

    private val _userFeatures = MutableStateFlow<List<UserAccessibilityFeature>>(emptyList())
    val userFeatures: StateFlow<List<UserAccessibilityFeature>> = _userFeatures

    init {
        loadMockUserFeatures()
    }

    private fun loadMockUserFeatures() {
        val userId = UUID.fromString("00000000-0000-0000-0000-000000000000")

        _userFeatures.value = listOf(
            UserAccessibilityFeature(
                id = UUID.randomUUID(),
                userId = userId,
                featureId = UUID.fromString("00000000-0000-0000-0000-000000000001"), // "No disabilities"
                createdAt = "2024-01-01T12:00:00",
                updatedAt = null
            ),
            UserAccessibilityFeature(
                id = UUID.randomUUID(),
                userId = userId,
                featureId = UUID.fromString("00000000-0000-0000-0000-000000000002"), // "Wheelchair Access"
                createdAt = "2024-01-01T12:10:00",
                updatedAt = null
            ),
            UserAccessibilityFeature(
                id = UUID.randomUUID(),
                userId = userId,
                featureId = UUID.fromString("00000000-0000-0000-0000-000000000003"), // "Audio Navigation"
                createdAt = "2024-01-01T12:15:00",
                updatedAt = null
            ),
            UserAccessibilityFeature(
                id = UUID.randomUUID(),
                userId = userId,
                featureId = UUID.fromString("00000000-0000-0000-0000-000000000004"), // "Visual Navigation"
                createdAt = "2024-01-01T12:20:00",
                updatedAt = null
            )
        )
    }

    fun setUserFeatures(selected: Set<UUID>) {
        val userId = UUID.fromString("00000000-0000-0000-0000-000000000000")
        val now = "2024-01-02T12:00:00"

        _userFeatures.value = selected.map {
            UserAccessibilityFeature(
                id = UUID.randomUUID(),
                userId = userId,
                featureId = it,
                createdAt = now,
                updatedAt = null
            )
        }
    }
}
