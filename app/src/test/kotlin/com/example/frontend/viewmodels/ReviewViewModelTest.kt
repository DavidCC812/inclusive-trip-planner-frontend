package com.example.frontend.viewmodels

import com.example.frontend.api.ReviewApi
import com.example.frontend.models.Review
import com.example.frontend.models.ReviewRequest
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
class ReviewViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var api: ReviewApi
    private lateinit var viewModel: ReviewViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        api = mockk()
        viewModel = ReviewViewModel(api, skipInitialFetch = true, logger = TestLogger)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchAllReviews should update reviews and clear error`() = runTest {
        val reviews = listOf(
            Review(
                id = UUID.randomUUID(),
                itineraryId = UUID.randomUUID(),
                userId = UUID.randomUUID(),
                rating = 4f,
                comment = "Very accessible!",
                createdAt = "2024-01-01T00:00:00",
                updatedAt = null
            )
        )
        coEvery { api.getAllReviews() } returns reviews

        viewModel.fetchAllReviews()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(reviews, viewModel.reviews.value)
        assertEquals(null, viewModel.error.value)
    }

    @Test
    fun `fetchAllReviews should handle API failure and set error`() = runTest {
        coEvery { api.getAllReviews() } throws RuntimeException("Network error")

        viewModel.fetchAllReviews()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(emptyList<Review>(), viewModel.reviews.value)
        assertEquals("Failed to fetch reviews", viewModel.error.value)
    }

    @Test
    fun `getReviewsForItinerary should return only matching reviews`() = runTest {
        val itineraryId = UUID.randomUUID()
        val irrelevantId = UUID.randomUUID()
        val expected = listOf(
            Review(
                id = UUID.randomUUID(),
                itineraryId = itineraryId,
                userId = UUID.randomUUID(),
                rating = 5f,
                comment = "Perfect!",
                createdAt = "2024-01-01T00:00:00",
                updatedAt = null
            )
        )
        val allReviews = expected + Review(
            id = UUID.randomUUID(),
            itineraryId = irrelevantId,
            userId = UUID.randomUUID(),
            rating = 2f,
            comment = "Too many stairs",
            createdAt = "2024-01-01T00:00:00",
            updatedAt = null
        )

        viewModel = ReviewViewModel(api, skipInitialFetch = true, initialReviews = allReviews, logger = TestLogger)

        val result = viewModel.getReviewsForItinerary(itineraryId.toString())

        assertEquals(expected, result)
    }

    @Test
    fun `postReview should add review to state and call onSuccess`() = runTest {
        val request = ReviewRequest(
            itineraryId = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            rating = 5f,
            comment = "Amazing place!"
        )
        val returnedReview = Review(
            id = UUID.randomUUID(),
            itineraryId = request.itineraryId,
            userId = request.userId,
            rating = request.rating,
            comment = request.comment,
            createdAt = "2024-01-01T00:00:00",
            updatedAt = null
        )

        coEvery { api.postReview(request) } returns returnedReview

        var successCalled = false
        viewModel.postReview(request,
            onSuccess = { successCalled = true },
            onError = { }
        )

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(listOf(returnedReview), viewModel.reviews.value)
        assertEquals(true, successCalled)
    }

    @Test
    fun `postReview should call onError if API fails`() = runTest {
        val request = ReviewRequest(
            itineraryId = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            rating = 3f,
            comment = "Meh"
        )

        coEvery { api.postReview(request) } throws RuntimeException("Server error")

        var errorMessage: String? = null
        viewModel.postReview(request,
            onSuccess = { },
            onError = { msg -> errorMessage = msg }
        )

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(emptyList<Review>(), viewModel.reviews.value)
        assertEquals("Server error", errorMessage)
    }
}