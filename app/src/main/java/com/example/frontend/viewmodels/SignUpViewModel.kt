package com.example.frontend.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.api.*
import com.example.frontend.models.*
import com.example.frontend.network.RetrofitClient
import com.example.frontend.storage.TokenManager
import com.example.frontend.util.AndroidLogger
import com.example.frontend.util.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class SignUpViewModel(
    application: Application,
    private val userApi: UserApi = RetrofitClient.userApi,
    private val authApi: AuthApi = RetrofitClient.authApi,
    private val destinationApi: DestinationApi = RetrofitClient.destinationApi,
    private val userAccessibilityFeatureApi: UserAccessibilityFeatureApi = RetrofitClient.userAccessibilityFeatureApi,
    private val userCountryAccessApi: UserCountryAccessApi = RetrofitClient.userCountryAccessApi,
    private val userSelectedDestinationApi: UserSelectedDestinationApi = RetrofitClient.userSelectedDestinationApi,
    private val tokenManager: TokenManager = TokenManager,
    private val logger: Logger = AndroidLogger
) : AndroidViewModel(application) {

    private val _fullName = MutableStateFlow("")
    val fullName: StateFlow<String> = _fullName

    private val _nickname = MutableStateFlow<String?>(null)
    val nickname: StateFlow<String?> = _nickname

    private val _phone = MutableStateFlow("")
    val phone: StateFlow<String> = _phone

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _selectedAccessibilityFeatures = MutableStateFlow<Set<UUID>>(emptySet())
    val selectedAccessibilityFeatures: StateFlow<Set<UUID>> = _selectedAccessibilityFeatures

    private val _selectedDestinations = MutableStateFlow<Set<String>>(emptySet())
    val selectedDestinations: StateFlow<Set<String>> = _selectedDestinations

    private val _selectedPlaces = MutableStateFlow<Set<String>>(emptySet())
    val selectedPlaces: StateFlow<Set<String>> = _selectedPlaces

    // Update functions
    fun updateFullName(name: String) { _fullName.value = name }
    fun updateNickname(nickname: String?) { _nickname.value = nickname }
    fun updatePhone(phone: String) { _phone.value = phone }
    fun updatePassword(password: String) { _password.value = password }
    fun updateEmail(email: String) {
        logger.d("SignUpViewModel", "Updating email to: $email")
        _email.value = email
    }
    fun updateSelectedAccessibilityFeatures(features: Set<UUID>) { _selectedAccessibilityFeatures.value = features }
    fun updateSelectedDestinations(destinations: Set<String>) {
        logger.d("SignUpViewModel", "Updating selected destinations with: $destinations")
        _selectedDestinations.value = destinations
    }
    fun updateSelectedPlaces(places: Set<String>) { _selectedPlaces.value = places }

    fun submitAccessibilityFeatures(userId: UUID) {
        viewModelScope.launch {
            selectedAccessibilityFeatures.value.forEach { featureId ->
                try {
                    val request = UserAccessibilityFeatureRequest(userId = userId, featureId = featureId)
                    userAccessibilityFeatureApi.createUserAccessibilityFeature(request)
                    logger.d("SignUpViewModel", "Posted feature $featureId for user $userId")
                } catch (e: Exception) {
                    logger.e("SignUpViewModel", "Failed to post feature $featureId for user $userId", e)
                }
            }
        }
    }

    fun submitUserCountryAccess(userId: UUID, countryNameToIdMap: Map<String, UUID>) {
        viewModelScope.launch {
            selectedDestinations.value.forEach { countryName ->
                val countryId = countryNameToIdMap[countryName]
                if (countryId != null) {
                    try {
                        val request = UserCountryAccessRequest(userId = userId, countryId = countryId)
                        userCountryAccessApi.createUserCountryAccess(request)
                        logger.d("SignUpViewModel", "Linked country $countryId for user $userId")
                    } catch (e: Exception) {
                        logger.e("SignUpViewModel", "Failed to link country $countryId for user $userId", e)
                    }
                }
            }
        }
    }

    fun submitUserSelectedDestinations(userId: UUID, destinationNameToIdMap: Map<String, UUID>) {
        viewModelScope.launch {
            selectedPlaces.value.forEach { destinationName ->
                val destinationId = destinationNameToIdMap[destinationName]
                if (destinationId != null) {
                    try {
                        val request = UserSelectedDestinationRequest(userId = userId, destinationId = destinationId)
                        userSelectedDestinationApi.createUserSelectedDestination(request)
                        logger.d("SignUpViewModel", "Linked destination $destinationId for user $userId")
                    } catch (e: Exception) {
                        logger.e("SignUpViewModel", "Failed to link destination $destinationId for user $userId", e)
                    }
                }
            }
        }
    }

    fun submitSignup(countryNameToIdMap: Map<String, UUID>) {
        viewModelScope.launch {
            try {
                logger.d("SignUpViewModel", "Preparing to submit signup...")

                val request = UserRequest(
                    name = fullName.value,
                    nickname = nickname.value,
                    email = email.value,
                    passwordHash = password.value,
                    phone = phone.value
                )

                val userResponse = userApi.createUser(request)
                logger.d("SignUpViewModel", "Signup successful: $userResponse")

                val loginRequest = LoginRequest(
                    identifier = request.email,
                    password = request.passwordHash
                )
                val loginResponse = authApi.login(loginRequest)

                tokenManager.saveToken(getApplication(), loginResponse.token)
                logger.d("SignUpViewModel", "Auto-login successful")

                val userId = UUID.fromString(loginResponse.userId)

                submitAccessibilityFeatures(userId)
                submitUserCountryAccess(userId, countryNameToIdMap)

                val allDestinations = destinationApi.getAllDestinations()
                val destinationNameToIdMap = allDestinations.associate { it.name to it.id }
                submitUserSelectedDestinations(userId, destinationNameToIdMap)

            } catch (e: Exception) {
                logger.e("SignUpViewModel", "Signup failed", e)
            }
        }
    }
}