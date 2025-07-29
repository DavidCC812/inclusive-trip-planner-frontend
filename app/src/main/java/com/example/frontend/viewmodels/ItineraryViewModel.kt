package com.example.frontend.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.models.Itinerary
import com.example.frontend.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import android.util.Log

class ItineraryViewModel(
    private val api: com.example.frontend.api.ItineraryApi = RetrofitClient.itineraryApi
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
                // Log.d("ItineraryFetch", "Fetched ${response.size} itineraries")
            } catch (e: Exception) {
                e.printStackTrace()
                // Log.e("ItineraryFetch", "Error fetching itineraries: ${e.message}")
            }
        }
    }
}
