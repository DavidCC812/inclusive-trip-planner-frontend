package com.example.frontend.viewmodels

import com.example.frontend.api.CountryApi
import com.example.frontend.models.Country
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
class CountryViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var api: CountryApi
    private lateinit var viewModel: CountryViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        api = mockk()
        viewModel = CountryViewModel(api)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchCountries should update countries and toggle loading`() = runTest {
        // Given
        val countries = listOf(
            Country(
                id = UUID.randomUUID(),
                name = "France",
                available = true,
                createdAt = "2024-01-01T00:00:00",
                updatedAt = "2024-01-01T00:00:00"
            ),
            Country(
                id = UUID.randomUUID(),
                name = "Japan",
                available = false,
                createdAt = "2024-01-01T00:00:00",
                updatedAt = "2024-01-01T00:00:00"
            )
        )
        coEvery { api.getAllCountries() } returns countries

        // When
        viewModel.fetchCountries()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(countries, viewModel.countries.value)
        assertEquals(false, viewModel.isLoading.value)
    }

    @Test
    fun `fetchCountries should handle API failure and stop loading`() = runTest {
        // Given
        coEvery { api.getAllCountries() } throws RuntimeException("Network error")

        // When
        viewModel.fetchCountries()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(emptyList<Country>(), viewModel.countries.value)
        assertEquals(false, viewModel.isLoading.value)
    }
}
