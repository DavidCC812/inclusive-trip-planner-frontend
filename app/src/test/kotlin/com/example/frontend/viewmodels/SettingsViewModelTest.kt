package com.example.frontend.viewmodels

import com.example.frontend.api.SettingApi
import com.example.frontend.api.UserSettingApi
import com.example.frontend.models.Setting
import com.example.frontend.models.UserSetting
import com.example.frontend.models.UserSettingUi
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
class SettingsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var settingApi: SettingApi
    private lateinit var userSettingApi: UserSettingApi
    private lateinit var viewModel: SettingsViewModel
    private val userId = UUID.randomUUID().toString()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        settingApi = mockk()
        userSettingApi = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadSettings should populate settings and clear error`() = runTest {
        val settingId = UUID.randomUUID().toString()
        val settingList = listOf(
            Setting(
                id = settingId,
                settingKey = "enable_notifications",
                label = "Enable notifications",
                description = "Toggle notifications on or off",
                defaultValue = true
            )
        )
        val userSettingList = listOf(
            UserSetting(
                id = "abc",
                userId = userId,
                settingId = settingId,
                value = false
            )
        )
        coEvery { settingApi.getAllSettings() } returns settingList
        coEvery { userSettingApi.getAllUserSettings() } returns userSettingList

        viewModel = SettingsViewModel(userId, settingApi, userSettingApi, skipInitFetch = true, logger = TestLogger)

        viewModel.loadSettings()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(
            listOf(
                UserSettingUi(
                    id = "abc",
                    settingId = settingId,
                    label = "Enable notifications",
                    value = false
                )
            ),
            viewModel.settings.value
        )
        assertEquals(null, viewModel.error.value)
    }

    @Test
    fun `loadSettings should handle API failure and set error`() = runTest {
        coEvery { settingApi.getAllSettings() } throws RuntimeException("API error")
        coEvery { userSettingApi.getAllUserSettings() } returns emptyList()

        viewModel = SettingsViewModel(userId, settingApi, userSettingApi, skipInitFetch = true, logger = TestLogger)
        viewModel.loadSettings()
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.error.value?.startsWith("Failed to load settings") == true)
        assertTrue(viewModel.settings.value.isEmpty())
    }

    @Test
    fun `postUserSetting should update existing user setting via PUT`() = runTest {
        val settingId = UUID.randomUUID().toString()
        val ui = UserSettingUi(
            id = "setting-123",
            settingId = settingId,
            label = "Enable something",
            value = false
        )
        coEvery { userSettingApi.updateUserSetting(eq("setting-123"), any()) } returns UserSetting(
            id = "setting-123",
            userId = userId,
            settingId = settingId,
            value = true
        )

        viewModel = SettingsViewModel(
            userId,
            settingApi,
            userSettingApi,
            initialSettings = listOf(ui),
            skipInitFetch = true,
            logger = TestLogger
        )

        viewModel.postUserSetting(settingId, true)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(true, viewModel.settings.value.first().value)
    }

    @Test
    fun `postUserSetting should create new user setting via POST`() = runTest {
        val settingId = UUID.randomUUID().toString()
        val ui = UserSettingUi(
            id = "", // triggers POST
            settingId = settingId,
            label = "New setting",
            value = false
        )
        coEvery { userSettingApi.postUserSetting(any()) } returns UserSetting(
            id = "setting-123",
            userId = userId,
            settingId = settingId,
            value = true
        )

        viewModel = SettingsViewModel(
            userId,
            settingApi,
            userSettingApi,
            initialSettings = listOf(ui),
            skipInitFetch = true,
            logger = TestLogger
        )

        viewModel.postUserSetting(settingId, true)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(true, viewModel.settings.value.first().value)
    }
}