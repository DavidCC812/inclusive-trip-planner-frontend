package com.example.frontend.viewmodels

import app.cash.turbine.test
import com.example.frontend.api.ItineraryApi
import com.example.frontend.models.Itinerary
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
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class ItineraryViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var api: ItineraryApi
    private lateinit var viewModel: ItineraryViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        api = mockk()
        viewModel = ItineraryViewModel(api = api, logger = TestLogger)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchItineraries should update state when API returns data`() = runTest {
        val expected = listOf(
            Itinerary(
                id = UUID.randomUUID(),
                title = "Louvre Tour",
                description = "Test description",
                price = 100.0,
                duration = 3,
                rating = 4.8f,
                createdAt = "2024-01-01T00:00:00",
                updatedAt = "2024-01-01T00:00:00",
                destinationName = "Paris",
                imageUrl = null
            )
        )
        coEvery { api.getAllItineraries() } returns expected

        viewModel.fetchItineraries()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(expected, viewModel.itineraries.value)
    }

    @Test
    fun `fetchItineraries should not crash and keep state empty on API failure`() = runTest {
        coEvery { api.getAllItineraries() } throws RuntimeException("Network error")

        viewModel.fetchItineraries()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(emptyList<Itinerary>(), viewModel.itineraries.value)
    }

    @Test
    fun `getItineraryById should return correct itinerary`() = runTest {
        val targetId = UUID.randomUUID()
        val expected = Itinerary(
            id = targetId,
            title = "Test Title",
            description = "desc",
            price = 25.0,
            duration = 2,
            rating = 4.2f,
            createdAt = "2024-01-01T00:00:00",
            updatedAt = "2024-01-01T00:00:00",
            destinationName = "Paris",
            imageUrl = null
        )

        coEvery { api.getAllItineraries() } returns listOf(expected)

        viewModel.fetchItineraries()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.getItineraryById(targetId.toString()).test {
            val item = awaitItem()
            assertEquals(expected, item)
            cancel()
        }
    }

    @Test
    fun `getItineraryById should return null if no match found`() = runTest {
        coEvery { api.getAllItineraries() } returns emptyList()
        viewModel.fetchItineraries()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.getItineraryById(UUID.randomUUID().toString()).test {
            val result = awaitItem()
            assertEquals(null, result)
            cancel()
        }
    }
}
