package com.example.frontend.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.frontend.models.ItineraryAccessibility
import java.util.UUID

class ItineraryAccessibilityViewModel : ViewModel() {

    private val _accessibilityLinks = MutableStateFlow<List<ItineraryAccessibility>>(emptyList())
    val accessibilityLinks: StateFlow<List<ItineraryAccessibility>> = _accessibilityLinks

    init {
        loadMockAccessibilityLinks()
    }

    private fun loadMockAccessibilityLinks() {
        val itineraryId = UUID.randomUUID()
        val wheelchairId = UUID.fromString("00000000-0000-0000-0000-000000000001")
        val hearingAidId = UUID.fromString("00000000-0000-0000-0000-000000000002")
        val elevatorId = UUID.fromString("00000000-0000-0000-0000-000000000003")

        _accessibilityLinks.value = listOf(
            ItineraryAccessibility(
                id = UUID.randomUUID(),
                itineraryId = itineraryId,
                featureId = wheelchairId,
                createdAt = "2024-01-01T10:00:00",
                updatedAt = null
            ),
            ItineraryAccessibility(
                id = UUID.randomUUID(),
                itineraryId = itineraryId,
                featureId = hearingAidId,
                createdAt = "2024-01-01T10:05:00",
                updatedAt = null
            ),
            ItineraryAccessibility(
                id = UUID.randomUUID(),
                itineraryId = itineraryId,
                featureId = elevatorId,
                createdAt = "2024-01-01T10:10:00",
                updatedAt = null
            )
        )
    }
}