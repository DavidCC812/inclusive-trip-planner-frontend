package com.example.frontend.viewmodels

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import java.util.UUID

class ProfileViewModel : ViewModel() {
    var userId = mutableStateOf(UUID.fromString("00000000-0000-0000-0000-000000009999")) // Replace with real userId when available
    var name = mutableStateOf("John Doe")
    var email = mutableStateOf("johndoe@example.com")
    var phone = mutableStateOf("+123 456 789")
    var accessibility = mutableStateOf("Wheelchair Accessible")

    fun updateProfile(
        newUserId: UUID,
        newName: String,
        newEmail: String,
        newPhone: String,
        newAccessibility: String
    ) {
        userId.value = newUserId
        name.value = newName
        email.value = newEmail
        phone.value = newPhone
        accessibility.value = newAccessibility
    }
}
