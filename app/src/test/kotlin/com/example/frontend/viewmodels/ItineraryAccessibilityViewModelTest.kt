package com.example.frontend.viewmodels

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ItineraryAccessibilityViewModelTest {

    @Test
    fun `accessibilityLinks should contain 3 items after init`() = runTest {
        val viewModel = ItineraryAccessibilityViewModel()

        val result = viewModel.accessibilityLinks.value

        assertEquals(3, result.size)
    }
}
