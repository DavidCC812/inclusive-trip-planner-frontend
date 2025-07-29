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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.frontend.components.CustomButton
import com.example.frontend.components.CustomInputField
import com.example.frontend.components.SignUpProgressBar
import com.example.frontend.viewmodels.SignUpViewModel

@Composable
fun SignUpNameScreen(
    navController: NavHostController,
) {
    val parentEntry = remember(navController) { navController.getBackStackEntry("signup_flow") }
    val signUpViewModel: SignUpViewModel = viewModel(parentEntry)

    var fullNameError by remember { mutableStateOf(false) }

    val fullName by signUpViewModel.fullName.collectAsState()
    val nickname by signUpViewModel.nickname.collectAsState()

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
            // Progress Bar
            Spacer(modifier = Modifier.height(20.dp))
            SignUpProgressBar(currentStep = 1, totalSteps = 5)

            // Title
            Spacer(modifier = Modifier.weight(0.5f))
            Text(
                text = "Enter Your Name",
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Inputs & Button
            Spacer(modifier = Modifier.weight(0.4f))

            CustomInputField(
                value = fullName,
                onValueChange = {
                    signUpViewModel.updateFullName(it)
                    fullNameError = it.isBlank()
                },
                label = "Full Name",
                isError = fullNameError,
                keyboardType = KeyboardType.Text,
                backgroundAlpha = 0.25f,
                textStyle = LocalTextStyle.current.copy(fontSize = 20.sp)
            )

            if (fullNameError) {
                Text(
                    text = "Full Name is required",
                    color = Color.Red,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp, bottom = 10.dp),
                    textAlign = TextAlign.Start
                )
            } else {
                Spacer(modifier = Modifier.height(12.dp))
            }

            CustomInputField(
                value = nickname.orEmpty(),
                onValueChange = { signUpViewModel.updateNickname(it) },
                label = "Nickname (Optional)",
                isError = false,
                keyboardType = KeyboardType.Text,
                backgroundAlpha = 0.25f,
                textStyle = LocalTextStyle.current.copy(fontSize = 20.sp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            val email by signUpViewModel.email.collectAsState()
            val phone by signUpViewModel.phone.collectAsState()

            CustomButton(
                text = "Next",
                enabled = fullName.isNotBlank(),
                fontWeight = if (fullName.isNotBlank()) FontWeight.Bold else FontWeight.Medium,
                onClick = {
                    when {
                        email.isNotBlank() && phone.isNotBlank() -> navController.navigate("signup_password")
                        email.isNotBlank() -> navController.navigate("signup_phone")
                        phone.isNotBlank() -> navController.navigate("signup_email")
                        else -> navController.navigate("signup_email")
                    }
                }
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
