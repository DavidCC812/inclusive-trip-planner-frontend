package com.example.frontend.viewmodels

import com.example.frontend.api.ItineraryStepApi
import com.example.frontend.models.ItineraryStep
import com.example.frontend.util.TestLogger
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class ItineraryStepViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var api: ItineraryStepApi
    private lateinit var viewModel: ItineraryStepViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        api = mockk()
        viewModel = ItineraryStepViewModel(api = api, logger = TestLogger)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchSteps should update steps and clear error`() = runTest {
        val itineraryId = UUID.randomUUID()
        val steps = listOf(
            ItineraryStep(
                id = UUID.randomUUID(),
                itineraryId = itineraryId,
                stepIndex = 1,
                title = "Step 1",
                description = "Visit museum",
                latitude = BigDecimal("48.8606"),
                longitude = BigDecimal("2.3376"),
                createdAt = "2024-01-01T00:00:00",
                updatedAt = null
            )
        )
        coEvery { api.getStepsForItinerary(itineraryId) } returns steps

        viewModel.fetchSteps(itineraryId)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(steps, viewModel.steps.value)
        assertEquals(null, viewModel.error.value)
    }

    @Test
    fun `fetchSteps should handle API failure and set error message`() = runTest {
        val itineraryId = UUID.randomUUID()
        coEvery { api.getStepsForItinerary(itineraryId) } throws RuntimeException("Network error")

        viewModel.fetchSteps(itineraryId)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(emptyList<ItineraryStep>(), viewModel.steps.value)
        assertEquals("No steps available or error occurred", viewModel.error.value)
    }
}
