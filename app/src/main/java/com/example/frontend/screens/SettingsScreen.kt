package com.example.frontend.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontend.viewmodels.SettingsViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import com.example.frontend.viewmodels.UserViewModel
import com.example.frontend.viewmodels.AccessibilityFeatureViewModel
import java.util.UUID
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.ui.text.input.KeyboardType
import com.example.frontend.components.CustomInputField
import com.example.frontend.components.SettingsActionButton
import com.example.frontend.viewmodels.ProfileViewModel

@Composable
fun SettingsScreen(navController: NavHostController, profileViewModel: ProfileViewModel) {
    val userViewModel: UserViewModel = viewModel()
    val user by userViewModel.user.collectAsState()
    val accessibilityFeatureViewModel: AccessibilityFeatureViewModel = viewModel()
    val selectedLabels by accessibilityFeatureViewModel.selectedLabels.collectAsState()
    val userId = user?.id

    // Step 1: Load user token once
    LaunchedEffect(Unit) {
        userViewModel.loadUserFromToken()
    }

    // Step 2: When user is ready, fetch their accessibility features
    LaunchedEffect(userId) {
        userId?.let {
            accessibilityFeatureViewModel.fetchUserFeatures(it)
        }
    }

    // Step 3: When both user and labels are ready, update the profileViewModel
    LaunchedEffect(userId, selectedLabels) {
        user?.let {
            profileViewModel.updateProfile(
                newUserId = UUID.fromString(it.id),
                newName = it.name,
                newEmail = it.email,
                newPhone = it.phone,
                newAccessibility = selectedLabels.joinToString(", ")
            )
        }
    }

    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var accessibility by remember { mutableStateOf("") }

    LaunchedEffect(profileViewModel.email.value, profileViewModel.phone.value, profileViewModel.accessibility.value) {
        email = profileViewModel.email.value
        phone = profileViewModel.phone.value
        accessibility = profileViewModel.accessibility.value
    }
    val name = profileViewModel.name.value

    var showDeleteDialog by remember { mutableStateOf(false) }

    val settingsViewModel: SettingsViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return SettingsViewModel(profileViewModel.userId.value.toString()) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    )
    val settings by settingsViewModel.settings.collectAsState()
    val isLoading by settingsViewModel.isLoading.collectAsState()
    val error by settingsViewModel.error.collectAsState()


    Scaffold(
        topBar = { SettingsTopBar(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF8FAFC),
                            Color(0xFFD9EAFD),
                            Color(0xFFBCCCDC)
                        )
                    )
                )
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileHeader(name = profileViewModel.name.value)
            EditableAccountDetails(
                name = name,
                email = email,
                phone = phone,
                accessibility = accessibility,
                onEmailChange = { email = it },
                onPhoneChange = { phone = it },
                onAccessibilityChange = { accessibility = it }
            )

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            if (isLoading) {
                CircularProgressIndicator()
            } else {
                settings.forEach { setting ->
                    SettingToggleItem(
                        title = setting.label,
                        description = if (setting.value) "Enabled" else "Disabled",
                        isChecked = setting.value,
                        onCheckedChange = {
                            settingsViewModel.postUserSetting(setting.settingId, it)
                        }
                    )
                }
            }

            SettingsActionButton(
                text = "Change Password",
                onClick = { navController.navigate("change_password") }
            )

            SettingsActionButton(
                text = "Privacy Policy",
                onClick = { navController.navigate("privacy_policy") }
            )

            SettingsActionButton(
                text = "Logout",
                onClick = { navController.navigate("welcome") },
                backgroundColor = Color(0xFFD32F2F),
                contentColor = Color.White,
                icon = {
                    Icon(
                        Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Logout",
                        modifier = Modifier.size(18.dp)
                    )
                }
            )

            SettingsActionButton(
                text = "Delete Account",
                onClick = { showDeleteDialog = true },
                backgroundColor = Color(0xFFD32F2F),
                contentColor = Color.White
            )
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete your account? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = { /* Add delete logic */ showDeleteDialog = false }) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun SettingsTopBar(navController: NavHostController) {
    TopAppBar(
        backgroundColor = Color(0xFF9AA6B2),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
            Text("Settings", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(48.dp))
        }
    }
}


@Composable
fun SettingToggleItem(
    title: String,
    description: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = description, fontSize = 14.sp, color = Color.DarkGray)
        }
        Switch(checked = isChecked, onCheckedChange = onCheckedChange)
    }
    Divider(color = Color.LightGray, thickness = 1.dp)
}

@Composable
fun EditableAccountDetails(
    name: String, email: String, phone: String, accessibility: String,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onAccessibilityChange: (String) -> Unit
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        Text("Edit Account Information", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        CustomInputField(
            value = name,
            onValueChange = {},
            label = "Name",
            isError = false,
            keyboardType = KeyboardType.Text,
            enabled = false
        )
        Spacer(modifier = Modifier.height(8.dp))

        CustomInputField(
            value = email,
            onValueChange = onEmailChange,
            label = "Email",
            isError = false,
            keyboardType = KeyboardType.Email
        )
        Spacer(modifier = Modifier.height(8.dp))

        CustomInputField(
            value = phone,
            onValueChange = onPhoneChange,
            label = "Phone",
            isError = false,
            keyboardType = KeyboardType.Phone
        )
        Spacer(modifier = Modifier.height(8.dp))

        CustomInputField(
            value = accessibility,
            onValueChange = onAccessibilityChange,
            label = "Accessibility Preferences",
            isError = false,
            keyboardType = KeyboardType.Text
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}