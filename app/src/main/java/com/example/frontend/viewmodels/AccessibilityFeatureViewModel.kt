package com.example.frontend.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.api.AccessibilityFeatureApi
import com.example.frontend.api.UserAccessibilityFeatureApi
import com.example.frontend.models.AccessibilityFeature
import com.example.frontend.network.RetrofitClient
import com.example.frontend.util.AndroidLogger
import com.example.frontend.util.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class AccessibilityFeatureViewModel(
    private val featureApi: AccessibilityFeatureApi = RetrofitClient.accessibilityFeatureApi,
    private val userFeatureApi: UserAccessibilityFeatureApi = RetrofitClient.userAccessibilityFeatureApi,
    initialFeatures: List<AccessibilityFeature> = emptyList(),
    skipInitFetch: Boolean = false,
    private val logger: Logger = AndroidLogger
) : ViewModel() {

    private val _features = MutableStateFlow(initialFeatures)
    val features: StateFlow<List<AccessibilityFeature>> = _features

    private val _selectedLabels = MutableStateFlow<List<String>>(emptyList())
    val selectedLabels: StateFlow<List<String>> = _selectedLabels

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        if (!skipInitFetch) {
            fetchAccessibilityFeatures()
        }
    }

    fun fetchAccessibilityFeatures() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = featureApi.getAccessibilityFeatures()
                _features.value = response
            } catch (e: Exception) {
                logger.e("AccessibilityFeature", "Fetch features error: ${e.message}", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchUserFeatures(userId: String) {
        viewModelScope.launch {
            try {
                val userFeatures = userFeatureApi.getAllUserAccessibilityFeatures()
                val selectedIds = userFeatures
                    .filter { it.userId == UUID.fromString(userId) }
                    .map { it.featureId }
                    .toSet()

                val selectedLabels = _features.value
                    .filter { it.id in selectedIds }
                    .map { it.name }

                _selectedLabels.value = selectedLabels
            } catch (e: Exception) {
                logger.e("AccessibilityFeature", "Fetch user features error: ${e.message}", e)
            }
        }
    }
}
