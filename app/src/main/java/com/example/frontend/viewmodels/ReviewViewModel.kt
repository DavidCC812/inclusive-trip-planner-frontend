package com.example.frontend.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.api.ReviewApi
import com.example.frontend.models.Review
import com.example.frontend.models.ReviewRequest
import com.example.frontend.network.RetrofitClient
import com.example.frontend.util.AndroidLogger
import com.example.frontend.util.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReviewViewModel(
    private val reviewApi: ReviewApi = RetrofitClient.reviewApi,
    skipInitialFetch: Boolean = false,
    initialReviews: List<Review> = emptyList(),
    private val logger: Logger = AndroidLogger
) : ViewModel() {

    private val _reviews = MutableStateFlow(initialReviews)
    val reviews: StateFlow<List<Review>> = _reviews

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        if (!skipInitialFetch) {
            fetchAllReviews()
        }
    }

    fun fetchAllReviews() {
        viewModelScope.launch {
            try {
                val response = reviewApi.getAllReviews()
                _reviews.value = response
                _error.value = null
                logger.d("ReviewViewModel", "Fetched ${response.size} reviews")
            } catch (e: Exception) {
                logger.e("ReviewViewModel", "Error fetching reviews: ${e.message}", e)
                _error.value = "Failed to fetch reviews"
            }
        }
    }

    fun getReviewsForItinerary(itineraryId: String): List<Review> {
        return _reviews.value.filter { it.itineraryId.toString() == itineraryId }
    }

    fun postReview(
        request: ReviewRequest,
        onSuccess: () -> Unit,
        onError: (String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = reviewApi.postReview(request)
                _reviews.value = _reviews.value + response
                onSuccess()
            } catch (e: Exception) {
                logger.e("ReviewViewModel", "Error posting review: ${e.message}", e)
                onError(e.message)
            }
        }
    }
}
