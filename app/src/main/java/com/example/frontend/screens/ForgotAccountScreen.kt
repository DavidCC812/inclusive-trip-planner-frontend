package com.example.frontend.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.frontend.components.BackButton
import com.example.frontend.components.CustomButton
import com.example.frontend.components.CustomInputField
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun ForgotAccountScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) }

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
            // Top Section
            Spacer(modifier = Modifier.height(16.dp))
            BackButton(navController)

            Spacer(modifier = Modifier.weight(0.5f))

            // Title
            Text(
                text = "Recover Your Account",
                fontSize = titleFontSize,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Description
            Text(
                text = "Enter your email address associated with your account, and we will send you a recovery link to regain access.",
                fontSize = descriptionFontSize,
                color = Color.Black.copy(alpha = 0.85f),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(bottom = 24.dp)
            )

            Spacer(modifier = Modifier.weight(0.3f))

            // Email Input Field
            CustomInputField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = ""
                },
                label = "Enter your email",
                isError = emailError.isNotEmpty(),
                keyboardType = KeyboardType.Email,
                backgroundAlpha = 0.2f,
                textStyle = LocalTextStyle.current.copy(fontSize = inputFontSize)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Error Message
            if (emailError.isNotEmpty()) {
                Text(
                    text = emailError,
                    color = Color.Red,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    textAlign = TextAlign.Start
                )
            }

            // Button
            Spacer(modifier = Modifier.height(24.dp))
            CustomButton(
                text = "Send Recovery Email",
                enabled = email.isNotEmpty(),
                onClick = {
                    if (email.isBlank()) {
                        emailError = "Email is required"
                    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        emailError = "Enter a valid email address"
                    } else {
                        isSubmitted = true
                    }
                },
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}