package com.example.frontend.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.frontend.R
import com.example.frontend.components.CustomButton
import com.example.frontend.components.PhoneInputField
import com.example.frontend.viewmodels.UserViewModel

@Composable
fun PhoneScreen(navController: NavHostController, viewModel: UserViewModel = viewModel()) {
    var phone by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf("") }

    val isLoading by viewModel.isLoading.collectAsState()

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
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo), // or whatever your filename is
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(160.dp)
            )

            Spacer(modifier = Modifier.height(210.dp))

            PhoneInputField(
                value = phone,
                onValueChange = {
                    phone = it
                    phoneError = ""
                },
                label = "Enter your phone number",
                isError = phoneError.isNotEmpty()
            )

            if (phoneError.isNotEmpty()) {
                Text(
                    text = phoneError,
                    color = Color.Red,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp, bottom = 10.dp),
                    textAlign = TextAlign.Start
                )
            } else {
                Spacer(modifier = Modifier.height(16.dp))
            }

            CustomButton(
                text = if (isLoading) "Checking..." else "Continue",
                enabled = phone.isNotEmpty() && !isLoading,
                onClick = {
                    if (phone.isBlank()) {
                        phoneError = "Phone number is required"
                    } else if (!phone.matches(Regex("^\\d{10,15}\$"))) {
                        phoneError = "Enter a valid phone number"
                    } else {
                        val normalizedPhone = phone.filter { it.isDigit() }

                        viewModel.fetchUserByPhone(normalizedPhone) { user ->
                            if (user != null) {
                                navController.navigate("login_with_password/${user.phone}")
                            } else {
                                navController.navigate("otp_verification/phone/$normalizedPhone")
                            }
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}
