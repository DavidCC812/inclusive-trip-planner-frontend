package com.example.frontend.models

data class Setting(
    val id: String,
    val settingKey: String,
    val label: String,
    val description: String,
    val defaultValue: Boolean
)
