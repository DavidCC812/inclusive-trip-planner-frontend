package com.example.frontend.viewmodels

import android.app.Application
import com.example.frontend.api.AuthApi
import com.example.frontend.api.UserApi
import com.example.frontend.models.*
import com.example.frontend.storage.TokenManager
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

@OptIn(ExperimentalCoroutinesApi::class)
class UserViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var userApi: UserApi
    private lateinit var authApi: AuthApi
    private lateinit var tokenManager: TokenManager
    private lateinit var viewModel: UserViewModel
    private lateinit var app: Application

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        userApi = mockk()
        authApi = mockk()
        tokenManager = mockk()
        app = mockk(relaxed = true)
        viewModel = UserViewModel(app, userApi, authApi, tokenManager, logger = TestLogger)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `createUser should update user state`() = runTest {
        val request = UserRequest(
            name = "John Doe",
            nickname = null,
            email = "john@example.com",
            passwordHash = "hashed",
            phone = "123456789"
        )
        val user = User(
            id = "1",
            name = "John Doe",
            nickname = null,
            email = "john@example.com",
            phone = "123456789",
            passwordHash = "hashed",
            createdAt = "2025-01-01T00:00:00",
            updatedAt = "2025-01-01T00:00:00"
        )
        coEvery { userApi.createUser(request) } returns user

        viewModel.createUser(request)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(user, viewModel.user.value)
        assertFalse(viewModel.isLoading.value)
        assertNull(viewModel.error.value)
    }

    @Test
    fun `fetchUser should update user state`() = runTest {
        val user = User(
            id = "1",
            name = "Alice",
            nickname = null,
            email = "alice@example.com",
            phone = "987654321",
            passwordHash = "pass",
            createdAt = "2025-01-01T00:00:00",
            updatedAt = "2025-01-01T00:00:00"
        )
        coEvery { userApi.getUserById("1") } returns user

        viewModel.fetchUser("1")
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(user, viewModel.user.value)
    }

    @Test
    fun `fetchUserByEmail should update state and call callback`() = runTest {
        val user = User(
            id = "2",
            name = "Bob",
            nickname = null,
            email = "bob@example.com",
            phone = "5550000",
            passwordHash = "hash",
            createdAt = "2025-01-01T00:00:00",
            updatedAt = "2025-01-01T00:00:00"
        )
        coEvery { userApi.getUserByEmail("bob@example.com") } returns user

        var result: User? = null
        viewModel.fetchUserByEmail("bob@example.com") { result = it }
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(user, result)
        assertEquals(user, viewModel.user.value)
    }

    @Test
    fun `login should store user on success`() = runTest {
        val response = AuthResponse(
            userId = "99",
            name = "Claire",
            email = "claire@example.com",
            phone = "111222333",
            token = "demo-token"
        )
        val request = LoginRequest("claire@example.com", "secure")

        coEvery { authApi.login(request) } returns response
        coEvery { userApi.getUserById("99") } returns User(
            id = "99",
            name = "Claire",
            nickname = null,
            email = "claire@example.com",
            phone = "111222333",
            passwordHash = "",
            createdAt = "",
            updatedAt = ""
        )
        coEvery { tokenManager.saveToken(any(), any()) } returns Unit

        var loginResult = false
        viewModel.login("claire@example.com", "secure") { loginResult = it }
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(loginResult)
        assertEquals("Claire", viewModel.user.value?.name)
    }
}