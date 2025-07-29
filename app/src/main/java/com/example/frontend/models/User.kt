package com.example.frontend.models

data class User(
    val id: String,
    val name: String,
    val nickname: String?,
    val email: String,
    val phone: String,
    val passwordHash: String,
    val createdAt: String,
    val updatedAt: String
)
