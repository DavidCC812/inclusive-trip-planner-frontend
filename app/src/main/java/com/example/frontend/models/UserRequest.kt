package com.example.frontend.models

data class UserRequest(
    val name: String,
    val nickname: String?,
    val email: String,
    val passwordHash: String,
    val phone: String
)
