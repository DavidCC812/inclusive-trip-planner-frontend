package com.example.frontend.models

data class UserSettingRequest(
    val userId: String,
    val settingId: String,
    val value: Boolean
)
