package com.example.frontend.models

data class AuthResponse(
    val token: String,
    val userId: String,
    val name: String,
    val email: String,
    val phone: String
)
