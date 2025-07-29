package com.example.frontend.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.frontend.components.BackButton
import com.example.frontend.components.CustomButton
import com.example.frontend.components.CustomInputField
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ChangePasswordScreen(navController: NavHostController) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var oldPasswordError by remember { mutableStateOf("") }
    var newPasswordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()
    var successMessage by remember { mutableStateOf<String?>(null) }

    val screenWidthDp = LocalConfiguration.current.screenWidthDp

    // Adaptive font sizes
    val titleFontSize = if (screenWidthDp > 600) 32.sp else 28.sp
    val descriptionFontSize = if (screenWidthDp > 600) 20.sp else 16.sp
    val inputFontSize = if (screenWidthDp > 600) 18.sp else 16.sp

    Surface(
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
            .padding(horizontal = 24.dp),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            BackButton(navController)

            Spacer(modifier = Modifier.weight(0.5f))

            // Title
            Text(
                text = "Change Your Password",
                fontSize = titleFontSize,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Description
            Text(
                text = "Enter your current password and set a new one.",
                fontSize = descriptionFontSize,
                color = Color.Black.copy(alpha = 0.85f),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(bottom = 24.dp)
            )

            Spacer(modifier = Modifier.weight(0.3f))

            // Input Fields
            CustomInputField(
                value = oldPassword,
                onValueChange = {
                    oldPassword = it
                    oldPasswordError = ""
                },
                label = "Enter current password",
                isError = oldPasswordError.isNotEmpty(),
                keyboardType = KeyboardType.Password,
                visualTransformation = PasswordVisualTransformation(),
                backgroundAlpha = 0.2f,
                textStyle = LocalTextStyle.current.copy(fontSize = inputFontSize)
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomInputField(
                value = newPassword,
                onValueChange = {
                    newPassword = it
                    newPasswordError = ""
                },
                label = "Enter new password",
                isError = newPasswordError.isNotEmpty(),
                keyboardType = KeyboardType.Password,
                visualTransformation = PasswordVisualTransformation(),
                backgroundAlpha = 0.2f,
                textStyle = LocalTextStyle.current.copy(fontSize = inputFontSize)
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomInputField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    confirmPasswordError = ""
                },
                label = "Confirm new password",
                isError = confirmPasswordError.isNotEmpty(),
                keyboardType = KeyboardType.Password,
                visualTransformation = PasswordVisualTransformation(),
                backgroundAlpha = 0.2f,
                textStyle = LocalTextStyle.current.copy(fontSize = inputFontSize)
            )

            // Error Messages
            Spacer(modifier = Modifier.height(8.dp))

            if (oldPasswordError.isNotEmpty()) {
                Text(
                    text = oldPasswordError,
                    color = Color.Red,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp),
                    textAlign = TextAlign.Start
                )
            }

            if (newPasswordError.isNotEmpty()) {
                Text(
                    text = newPasswordError,
                    color = Color.Red,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp),
                    textAlign = TextAlign.Start
                )
            }

            if (confirmPasswordError.isNotEmpty()) {
                Text(
                    text = confirmPasswordError,
                    color = Color.Red,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp),
                    textAlign = TextAlign.Start
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Change Password Button
            CustomButton(
                text = "Change Password",
                enabled = oldPassword.isNotEmpty() && newPassword.isNotEmpty() && confirmPassword.isNotEmpty(),
                onClick = {
                    var isValid = true

                    if (oldPassword.isBlank()) {
                        oldPasswordError = "Current password is required"
                        isValid = false
                    }

                    if (newPassword.isBlank()) {
                        newPasswordError = "New password is required"
                        isValid = false
                    } else if (newPassword.length < 6) {
                        newPasswordError = "Password must be at least 6 characters"
                        isValid = false
                    }

                    if (confirmPassword.isBlank()) {
                        confirmPasswordError = "Please confirm your new password"
                        isValid = false
                    } else if (confirmPassword != newPassword) {
                        confirmPasswordError = "Passwords do not match"
                        isValid = false
                    }

                    if (isValid) {
                        successMessage = "Password changed successfully!"
                        coroutineScope.launch {
                            delay(2000)
                            navController.popBackStack()
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            successMessage?.let {
                Text(
                    it,
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}