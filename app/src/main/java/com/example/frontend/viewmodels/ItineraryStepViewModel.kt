package com.example.frontend.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.api.ItineraryStepApi
import com.example.frontend.models.ItineraryStep
import com.example.frontend.network.RetrofitClient
import com.example.frontend.util.AndroidLogger
import com.example.frontend.util.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class ItineraryStepViewModel(
    private val api: ItineraryStepApi = RetrofitClient.itineraryStepApi,
    private val logger: Logger = AndroidLogger
) : ViewModel() {

    private val _steps = MutableStateFlow<List<ItineraryStep>>(emptyList())
    val steps: StateFlow<List<ItineraryStep>> = _steps

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchSteps(itineraryId: UUID) {
        viewModelScope.launch {
            try {
                val response = api.getStepsForItinerary(itineraryId)
                _steps.value = response
                _error.value = null
                logger.d("ItineraryStepFetch", "Fetched ${response.size} steps for $itineraryId")
            } catch (e: Exception) {
                logger.e("ItineraryStepFetch", "Error fetching steps: ${e.message}", e)
                _error.value = "No steps available or error occurred"
            }
        }
    }
}
