package com.example.frontend.viewmodels

import com.example.frontend.api.AccessibilityFeatureApi
import com.example.frontend.api.UserAccessibilityFeatureApi
import com.example.frontend.models.AccessibilityFeature
import kotlinx.coroutines.flow.MutableStateFlow
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class AccessibilityFeatureViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var featureApi: AccessibilityFeatureApi
    private lateinit var userFeatureApi: UserAccessibilityFeatureApi
    private lateinit var viewModel: AccessibilityFeatureViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        featureApi = mockk()
        userFeatureApi = mockk()
        viewModel = AccessibilityFeatureViewModel(featureApi, userFeatureApi)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchAccessibilityFeatures should update features and toggle loading`() = runTest {
        // Given
        val features = listOf(
            AccessibilityFeature(
                id = UUID.randomUUID(),
                name = "Wheelchair Access",
                createdAt = "2024-01-01T00:00:00",
                updatedAt = "2024-01-01T00:00:00"
            ),
            AccessibilityFeature(
                id = UUID.randomUUID(),
                name = "Sign Language Guide",
                createdAt = "2024-01-01T00:00:00",
                updatedAt = "2024-01-01T00:00:00"
            )
        )
        coEvery { featureApi.getAccessibilityFeatures() } returns features

        // When
        viewModel.fetchAccessibilityFeatures()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(features, viewModel.features.value)
        assertEquals(false, viewModel.isLoading.value)
    }
    @Test
    fun `fetchAccessibilityFeatures should handle API failure and stop loading`() = runTest {
        // Given
        coEvery { featureApi.getAccessibilityFeatures() } throws RuntimeException("Network failure")

        // When
        viewModel.fetchAccessibilityFeatures()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(emptyList<AccessibilityFeature>(), viewModel.features.value)
        assertEquals(false, viewModel.isLoading.value)
    }
    @Test
    fun `fetchUserFeatures should update selected labels for given user`() = runTest {
        // Given
        val userId = UUID.randomUUID()
        val feature1 = AccessibilityFeature(
            id = UUID.randomUUID(),
            name = "Wheelchair Access",
            createdAt = "2024-01-01T00:00:00",
            updatedAt = "2024-01-01T00:00:00"
        )
        val feature2 = AccessibilityFeature(
            id = UUID.randomUUID(),
            name = "Sign Language",
            createdAt = "2024-01-01T00:00:00",
            updatedAt = "2024-01-01T00:00:00"
        )

        val userFeatures = listOf(
            com.example.frontend.models.UserAccessibilityFeature(
                id = UUID.randomUUID(),
                userId = userId,
                featureId = feature2.id,
                createdAt = "2024-01-01T00:00:00",
                updatedAt = "2024-01-01T00:00:00"
            )
        )

        // Mock before ViewModel creation
        coEvery { featureApi.getAccessibilityFeatures() } returns emptyList()
        coEvery { userFeatureApi.getAllUserAccessibilityFeatures() } returns userFeatures

        viewModel = AccessibilityFeatureViewModel(
            featureApi = featureApi,
            userFeatureApi = userFeatureApi,
            initialFeatures = listOf(feature1, feature2),
            skipInitFetch = true
        )

        // When
        viewModel.fetchUserFeatures(userId.toString())
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(listOf("Sign Language"), viewModel.selectedLabels.value)
    }
}
