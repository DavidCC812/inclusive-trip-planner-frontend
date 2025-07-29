package com.example.frontend.viewmodels

import com.example.frontend.api.SavedItineraryApi
import com.example.frontend.models.SavedItinerary
import com.example.frontend.util.TestLogger
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class SavedItinerariesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var api: SavedItineraryApi
    private lateinit var viewModel: SavedItinerariesViewModel

    private val userId = UUID.fromString("00000000-0000-0000-0000-000000009999")

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        api = mockk()
        viewModel = SavedItinerariesViewModel(api, skipInitialFetch = true, logger = TestLogger)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchSavedItineraries should update state and clear error`() = runTest {
        val expected = listOf(
            SavedItinerary(
                id = UUID.randomUUID(),
                userId = userId,
                itineraryId = UUID.randomUUID(),
                savedAt = "2024-01-01T00:00:00",
                createdAt = "2024-01-01T00:00:00",
                updatedAt = null
            )
        )
        coEvery { api.getByUserId(userId) } returns expected

        viewModel.fetchSavedItineraries()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(expected, viewModel.savedItineraries.value)
        assertNull(viewModel.error.value)
    }

    @Test
    fun `fetchSavedItineraries should handle failure and set error`() = runTest {
        coEvery { api.getByUserId(userId) } throws RuntimeException("Network issue")

        viewModel.fetchSavedItineraries()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(emptyList<SavedItinerary>(), viewModel.savedItineraries.value)
        assertEquals("Error fetching saved itineraries", viewModel.error.value)
    }

    @Test
    fun `saveItinerary should add saved itinerary and call onSuccess`() = runTest {
        val itineraryId = UUID.randomUUID()
        val newItem = SavedItinerary(
            id = UUID.randomUUID(),
            userId = userId,
            itineraryId = UUID.randomUUID(),
            savedAt = "2024-01-01T00:00:00",
            createdAt = "2024-01-01T00:00:00",
            updatedAt = null
        )
        coEvery { api.saveItinerary(any()) } returns newItem

        var successCalled = false

        viewModel.saveItinerary(itineraryId) {
            successCalled = true
        }

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(listOf(newItem), viewModel.savedItineraries.value)
        assertTrue(successCalled)
    }

    @Test
    fun `removeItinerary should delete existing item from state`() = runTest {
        val itineraryId = UUID.randomUUID()
        val saved = SavedItinerary(
            id = UUID.randomUUID(),
            userId = userId,
            itineraryId = itineraryId,
            savedAt = "2024-01-01T00:00:00",
            createdAt = "2024-01-01T00:00:00",
            updatedAt = null
        )
        coEvery { api.deleteItinerary(saved.id) } returns Unit

        viewModel = SavedItinerariesViewModel(
            api,
            skipInitialFetch = true,
            initialSaved = listOf(saved),
            logger = TestLogger
        )

        viewModel.removeItinerary(itineraryId)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.savedItineraries.value.isEmpty())
    }

    @Test
    fun `setAsNextPlan should assign correct item as next plan`() = runTest {
        val itineraryId = UUID.randomUUID()
        val saved = SavedItinerary(
            id = UUID.randomUUID(),
            userId = userId,
            itineraryId = itineraryId,
            savedAt = "2024-01-01T00:00:00",
            createdAt = "2024-01-01T00:00:00",
            updatedAt = null
        )

        viewModel = SavedItinerariesViewModel(
            api,
            skipInitialFetch = true,
            initialSaved = listOf(saved),
            logger = TestLogger
        )

        viewModel.setAsNextPlan(itineraryId)

        assertEquals(saved, viewModel.nextPlan.value)
    }
}