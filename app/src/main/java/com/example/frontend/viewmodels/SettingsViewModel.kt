package com.example.frontend.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.api.SettingApi
import com.example.frontend.api.UserSettingApi
import com.example.frontend.models.UserSettingRequest
import com.example.frontend.models.UserSettingUi
import com.example.frontend.network.RetrofitClient
import com.example.frontend.util.AndroidLogger
import com.example.frontend.util.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val userId: String,
    private val settingApi: SettingApi = RetrofitClient.settingApi,
    private val userSettingApi: UserSettingApi = RetrofitClient.userSettingApi,
    initialSettings: List<UserSettingUi> = emptyList(),
    skipInitFetch: Boolean = false,
    private val logger: Logger = AndroidLogger
) : ViewModel() {

    private val _settings = MutableStateFlow(initialSettings)
    val settings: StateFlow<List<UserSettingUi>> = _settings

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        if (!skipInitFetch) {
            loadSettings()
        }
    }

    fun loadSettings() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val allSettings = settingApi.getAllSettings()
                val userSettings = userSettingApi.getAllUserSettings()

                val settingMap = userSettings.associateBy { it.settingId }

                val uiList = allSettings.map { setting ->
                    val userSetting = settingMap[setting.id]
                    UserSettingUi(
                        id = userSetting?.id ?: "",
                        settingId = setting.id,
                        label = setting.label,
                        value = userSetting?.value ?: setting.defaultValue
                    )
                }

                _settings.value = uiList
            } catch (e: Exception) {
                _error.value = "Failed to load settings: ${e.localizedMessage}"
                logger.e("SettingsViewModel", "Error loading settings: ${e.message}", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun postUserSetting(settingId: String, value: Boolean) {
        viewModelScope.launch {
            try {
                val existing = _settings.value.find { it.settingId == settingId }
                val userSettingId = existing?.id ?: ""
                val request = UserSettingRequest(
                    userId = userId,
                    settingId = settingId,
                    value = value
                )

                if (userSettingId.isNotEmpty()) {
                    userSettingApi.updateUserSetting(userSettingId, request)
                    logger.d("SettingsViewModel", "Updated setting via PUT: $settingId = $value")
                } else {
                    userSettingApi.postUserSetting(request)
                    logger.d("SettingsViewModel", "Created setting via POST: $settingId = $value")
                }

                // Update local UI
                val updatedList = _settings.value.map {
                    if (it.settingId == settingId) it.copy(value = value) else it
                }
                _settings.value = updatedList

            } catch (e: Exception) {
                logger.e("SettingsViewModel", "Failed to post/update setting", e)
            }
        }
    }
}
