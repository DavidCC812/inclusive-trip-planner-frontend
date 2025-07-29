package com.example.frontend.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.api.ItineraryApi
import com.example.frontend.models.Itinerary
import com.example.frontend.network.RetrofitClient
import com.example.frontend.util.AndroidLogger
import com.example.frontend.util.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ItineraryViewModel(
    private val api: ItineraryApi = RetrofitClient.itineraryApi,
    private val logger: Logger = AndroidLogger
) : ViewModel() {

    private val _itineraries = MutableStateFlow<List<Itinerary>>(emptyList())
    val itineraries: StateFlow<List<Itinerary>> = _itineraries

    init {
        fetchItineraries()
    }

    fun getItineraryById(itineraryId: String): Flow<Itinerary?> {
        return itineraries.map { list ->
            list.find { it.id.toString() == itineraryId }
        }
    }

    fun fetchItineraries() {
        viewModelScope.launch {
            try {
                val response = api.getAllItineraries()
                _itineraries.value = response
                logger.d("ItineraryFetch", "Fetched ${response.size} itineraries")
            } catch (e: Exception) {
                logger.e("ItineraryFetch", "Error fetching itineraries: ${e.message}", e)
            }
        }
    }
}
