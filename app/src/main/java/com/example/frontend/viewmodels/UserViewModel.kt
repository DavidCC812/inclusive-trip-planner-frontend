package com.example.frontend.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.api.AuthApi
import com.example.frontend.api.UserApi
import com.example.frontend.models.*
import com.example.frontend.network.RetrofitClient
import com.example.frontend.storage.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class UserViewModel(
    application: Application,
    private val userApi: UserApi = RetrofitClient.userApi,
    private val authApi: AuthApi = RetrofitClient.authApi,
    private val tokenManager: TokenManager = TokenManager
) : AndroidViewModel(application) {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun createUser(request: UserRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = userApi.createUser(request)
                _user.value = response
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Unexpected error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchUser(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = userApi.getUserById(id)
                _user.value = response
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Unable to load user"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchUserByEmail(email: String, onResult: (User?) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = userApi.getUserByEmail(email)
                _user.value = response
                onResult(response)
            } catch (e: Exception) {
                //Log.e("UserViewModel", "Error fetching user by email: ${e.localizedMessage}", e)
                _user.value = null
                onResult(null)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchUserByPhone(phone: String, onResult: (User?) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = userApi.getUserByPhone(phone)
                _user.value = response
                onResult(response)
            } catch (e: Exception) {
                //Log.e("UserViewModel", "Error fetching user by phone: ${e.localizedMessage}", e)
                _user.value = null
                onResult(null)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun login(emailOrPhone: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val request = LoginRequest(identifier = emailOrPhone, password = password)
                val response = authApi.login(request)

                tokenManager.saveToken(getApplication(), response.token)

                _user.value = User(
                    id = response.userId,
                    name = response.name,
                    nickname = null,
                    email = response.email,
                    phone = response.phone,
                    passwordHash = "",
                    createdAt = "",
                    updatedAt = ""
                )

                onResult(true)
            } catch (e: Exception) {
                //Log.e("UserViewModel", "Login failed: ${e.localizedMessage}", e)
                _error.value = "Login failed: ${e.localizedMessage}"
                _user.value = null
                onResult(false)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadUserFromToken() {
        viewModelScope.launch {
            try {
                val token = tokenManager.getToken(getApplication()).firstOrNull()
                if (token != null) {
                    val payload = tokenManager.decodeTokenPayload(token)
                    val userId = payload?.get("userId")?.toString()

                    if (userId != null) {
                        val response = userApi.getUserById(userId)
                        _user.value = response
                    }
                }
            } catch (e: Exception) {
                //Log.e("UserViewModel", "Failed to load user from token", e)
            }
        }
    }
}