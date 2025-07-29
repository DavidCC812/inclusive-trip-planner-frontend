package com.example.frontend.viewmodels

import android.app.Application
import com.example.frontend.api.*
import com.example.frontend.models.*
import com.example.frontend.storage.TokenManager
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
class SignUpViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var app: Application
    private lateinit var viewModel: SignUpViewModel

    private lateinit var userApi: UserApi
    private lateinit var authApi: AuthApi
    private lateinit var destinationApi: DestinationApi
    private lateinit var userAccessibilityFeatureApi: UserAccessibilityFeatureApi
    private lateinit var userCountryAccessApi: UserCountryAccessApi
    private lateinit var userSelectedDestinationApi: UserSelectedDestinationApi
    private lateinit var tokenManager: TokenManager

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        app = mockk(relaxed = true)
        userApi = mockk()
        authApi = mockk()
        destinationApi = mockk()
        userAccessibilityFeatureApi = mockk()
        userCountryAccessApi = mockk()
        userSelectedDestinationApi = mockk()
        tokenManager = mockk()

        viewModel = SignUpViewModel(
            app,
            userApi,
            authApi,
            destinationApi,
            userAccessibilityFeatureApi,
            userCountryAccessApi,
            userSelectedDestinationApi,
            tokenManager,
            logger = TestLogger
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `updateFullName should change fullName state`() = runTest {
        viewModel.updateFullName("John Doe")
        assertEquals("John Doe", viewModel.fullName.value)
    }

    @Test
    fun `updateNickname should change nickname state`() = runTest {
        viewModel.updateNickname("Johnny")
        assertEquals("Johnny", viewModel.nickname.value)
    }

    @Test
    fun `updatePhone should change phone state`() = runTest {
        viewModel.updatePhone("123456789")
        assertEquals("123456789", viewModel.phone.value)
    }

    @Test
    fun `updatePassword should change password state`() = runTest {
        viewModel.updatePassword("secure123")
        assertEquals("secure123", viewModel.password.value)
    }

    @Test
    fun `updateEmail should change email state`() = runTest {
        viewModel.updateEmail("john@example.com")
        assertEquals("john@example.com", viewModel.email.value)
    }

    @Test
    fun `updateSelectedAccessibilityFeatures should change accessibility features state`() = runTest {
        val features = setOf(UUID.randomUUID(), UUID.randomUUID())
        viewModel.updateSelectedAccessibilityFeatures(features)
        assertEquals(features, viewModel.selectedAccessibilityFeatures.value)
    }

    @Test
    fun `updateSelectedDestinations should change selected destinations state`() = runTest {
        val destinations = setOf("Paris", "Rome")
        viewModel.updateSelectedDestinations(destinations)
        assertEquals(destinations, viewModel.selectedDestinations.value)
    }

    @Test
    fun `updateSelectedPlaces should change selected places state`() = runTest {
        val places = setOf("Louvre", "Colosseum")
        viewModel.updateSelectedPlaces(places)
        assertEquals(places, viewModel.selectedPlaces.value)
    }

    @Test
    fun `submitAccessibilityFeatures should post each selected feature`() = runTest {
        val userId = UUID.randomUUID()
        val feature1 = UUID.randomUUID()
        val feature2 = UUID.randomUUID()
        val features = setOf(feature1, feature2)

        viewModel.updateSelectedAccessibilityFeatures(features)

        coEvery {
            userAccessibilityFeatureApi.createUserAccessibilityFeature(
                UserAccessibilityFeatureRequest(userId, feature1)
            )
        } returns UserAccessibilityFeature(
            id = UUID.randomUUID(),
            userId = userId,
            featureId = feature1,
            createdAt = "2025-01-01T00:00:00",
            updatedAt = "2025-01-01T00:00:00"
        )

        coEvery {
            userAccessibilityFeatureApi.createUserAccessibilityFeature(
                UserAccessibilityFeatureRequest(userId, feature2)
            )
        } returns UserAccessibilityFeature(
            id = UUID.randomUUID(),
            userId = userId,
            featureId = feature2,
            createdAt = "2025-01-01T00:00:00",
            updatedAt = "2025-01-01T00:00:00"
        )

        viewModel.submitAccessibilityFeatures(userId)
        testDispatcher.scheduler.advanceUntilIdle()
    }

    @Test
    fun `submitUserCountryAccess should post each selected country`() = runTest {
        val userId = UUID.randomUUID()
        val franceId = UUID.randomUUID()
        val italyId = UUID.randomUUID()
        val countryMap = mapOf("France" to franceId, "Italy" to italyId)

        viewModel.updateSelectedDestinations(setOf("France", "Italy"))

        coEvery {
            userCountryAccessApi.createUserCountryAccess(
                UserCountryAccessRequest(userId, franceId)
            )
        } returns UserCountryAccess(
            id = UUID.randomUUID(),
            userId = userId,
            countryId = franceId,
            countryName = "France",
            createdAt = "2024-01-01T00:00:00",
            updatedAt = "2024-01-01T00:00:00"
        )

        coEvery {
            userCountryAccessApi.createUserCountryAccess(
                UserCountryAccessRequest(userId, italyId)
            )
        } returns UserCountryAccess(
            id = UUID.randomUUID(),
            userId = userId,
            countryId = italyId,
            countryName = "Italy",
            createdAt = "2024-01-01T00:00:00",
            updatedAt = "2024-01-01T00:00:00"
        )

        viewModel.submitUserCountryAccess(userId, countryMap)
        testDispatcher.scheduler.advanceUntilIdle()
    }

    @Test
    fun `submitUserSelectedDestinations should post each selected place`() = runTest {
        val userId = UUID.randomUUID()
        val louvreId = UUID.randomUUID()
        val colosseumId = UUID.randomUUID()
        val destinationMap = mapOf("Louvre" to louvreId, "Colosseum" to colosseumId)

        viewModel.updateSelectedPlaces(setOf("Louvre", "Colosseum"))

        coEvery {
            userSelectedDestinationApi.createUserSelectedDestination(
                UserSelectedDestinationRequest(userId, louvreId)
            )
        } returns UserSelectedDestination(
            id = UUID.randomUUID(),
            userId = userId,
            destinationId = louvreId,
            createdAt = "2024-01-01T00:00:00",
            updatedAt = "2024-01-01T00:00:00"
        )

        coEvery {
            userSelectedDestinationApi.createUserSelectedDestination(
                UserSelectedDestinationRequest(userId, colosseumId)
            )
        } returns UserSelectedDestination(
            id = UUID.randomUUID(),
            userId = userId,
            destinationId = colosseumId,
            createdAt = "2024-01-01T00:00:00",
            updatedAt = "2024-01-01T00:00:00"
        )

        viewModel.submitUserSelectedDestinations(userId, destinationMap)
        testDispatcher.scheduler.advanceUntilIdle()
    }

    @Test
    fun `submitSignup should complete full signup flow`() = runTest {
        val userId = UUID.randomUUID()
        val louvreId = UUID.randomUUID()
        val franceId = UUID.randomUUID()

        viewModel.updateFullName("Alice")
        viewModel.updateNickname("Ally")
        viewModel.updateEmail("alice@example.com")
        viewModel.updatePassword("pass")
        viewModel.updatePhone("123")
        viewModel.updateSelectedAccessibilityFeatures(setOf(UUID.randomUUID()))
        viewModel.updateSelectedDestinations(setOf("France"))
        viewModel.updateSelectedPlaces(setOf("Louvre"))

        coEvery { userApi.createUser(any()) } returns mockk(relaxed = true)
        coEvery { authApi.login(any()) } returns AuthResponse(
            userId = userId.toString(),
            name = "Alice",
            email = "alice@example.com",
            phone = "123",
            token = "fake-token"
        )
        coEvery { tokenManager.saveToken(any(), any()) } returns Unit
        coEvery { userAccessibilityFeatureApi.createUserAccessibilityFeature(any()) } returns mockk(relaxed = true)
        coEvery { userCountryAccessApi.createUserCountryAccess(any()) } returns mockk(relaxed = true)
        coEvery { destinationApi.getAllDestinations() } returns listOf(
            Destination(
                id = louvreId,
                name = "Louvre",
                type = "museum",
                available = true,
                countryId = franceId,
                countryName = "France",
                createdAt = "2024-01-01T00:00:00",
                updatedAt = "2024-01-01T00:00:00"
            )
        )
        coEvery { userSelectedDestinationApi.createUserSelectedDestination(any()) } returns mockk(relaxed = true)

        viewModel.submitSignup(mapOf("France" to franceId))
        testDispatcher.scheduler.advanceUntilIdle()
    }
}