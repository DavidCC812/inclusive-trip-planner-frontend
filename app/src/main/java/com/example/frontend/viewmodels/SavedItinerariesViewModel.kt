package com.example.frontend.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.api.SavedItineraryApi
import com.example.frontend.models.SavedItinerary
import com.example.frontend.models.SavedItineraryRequest
import com.example.frontend.network.RetrofitClient
import com.example.frontend.util.AndroidLogger
import com.example.frontend.util.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class SavedItinerariesViewModel(
    private val api: SavedItineraryApi = RetrofitClient.savedItineraryApi,
    private val userId: UUID = UUID.fromString("00000000-0000-0000-0000-000000009999"), // Demo user
    initialSaved: List<SavedItinerary> = emptyList(),
    skipInitialFetch: Boolean = false,
    private val logger: Logger = AndroidLogger
) : ViewModel() {

    private val _savedItineraries = MutableStateFlow(initialSaved)
    val savedItineraries: StateFlow<List<SavedItinerary>> = _savedItineraries

    private val _nextPlan = MutableStateFlow<SavedItinerary?>(null)
    val nextPlan: StateFlow<SavedItinerary?> = _nextPlan

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        if (!skipInitialFetch) {
            fetchSavedItineraries()
        }
    }

    fun fetchSavedItineraries() {
        viewModelScope.launch {
            try {
                val response = api.getByUserId(userId)
                _savedItineraries.value = response
                _error.value = null
            } catch (e: Exception) {
                logger.e("SavedItineraries", "Fetch error: ${e.message}")
                _error.value = "Error fetching saved itineraries"
            }
        }
    }

    fun saveItinerary(itineraryId: UUID, onSuccess: (() -> Unit)? = null) {
        viewModelScope.launch {
            try {
                logger.d("SavedItineraries", "Attempting to save itinerary $itineraryId")

                val request = SavedItineraryRequest(userId, itineraryId)
                val created = api.saveItinerary(request)

                logger.d("SavedItineraries", "Successfully saved: $created")

                _savedItineraries.value = _savedItineraries.value + created
                onSuccess?.invoke()
            } catch (e: Exception) {
                logger.e("SavedItineraries", "Save error: ${e.message}", e)
            }
        }
    }

    fun removeItinerary(itineraryId: UUID) {
        viewModelScope.launch {
            val existing = _savedItineraries.value.find { it.itineraryId == itineraryId }
            if (existing != null) {
                try {
                    api.deleteItinerary(existing.id)
                    _savedItineraries.value = _savedItineraries.value.filter { it.id != existing.id }
                } catch (e: Exception) {
                    logger.e("SavedItineraries", "Delete error: ${e.message}")
                }
            }
        }
    }

    fun setAsNextPlan(itineraryId: UUID) {
        logger.d("SavedItineraries", "Looking for itineraryId $itineraryId in: ${_savedItineraries.value.map { it.itineraryId }}")
        val item = _savedItineraries.value.find { it.itineraryId == itineraryId }
        if (item != null) {
            _nextPlan.value = item
            logger.d("SavedItineraries", "Set $itineraryId as next plan")
        }
    }
}
