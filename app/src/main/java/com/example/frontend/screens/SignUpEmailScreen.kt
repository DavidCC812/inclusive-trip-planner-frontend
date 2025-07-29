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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.frontend.components.BackButton
import com.example.frontend.components.CustomButton
import com.example.frontend.components.CustomInputField
import com.example.frontend.components.SignUpProgressBar
import com.example.frontend.viewmodels.SignUpViewModel
import androidx.compose.ui.text.input.KeyboardType


@Composable
fun SignUpEmailScreen(navController: NavHostController) {
    val parentEntry = remember(navController) { navController.getBackStackEntry("signup_flow") }
    val signUpViewModel: SignUpViewModel = viewModel(parentEntry)

    var isSubmitted by remember { mutableStateOf(false) }
    val email by signUpViewModel.email.collectAsState()

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
            Spacer(modifier = Modifier.height(20.dp))
            SignUpProgressBar(currentStep = 2, totalSteps = 5)
            BackButton(navController)

            Spacer(modifier = Modifier.weight(0.5f))

            Text(
                text = "Enter your email",
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Spacer(modifier = Modifier.weight(0.4f))

            CustomInputField(
                value = email,
                onValueChange = {
                    signUpViewModel.updateEmail(it)
                    isSubmitted = false
                },
                label = "Email",
                isError = isSubmitted && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches(),
                keyboardType = KeyboardType.Email
            )

            if (isSubmitted && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Text(
                    text = "Enter a valid email address",
                    color = Color.Red,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp, bottom = 10.dp),
                    textAlign = TextAlign.Start
                )
            } else {
                Spacer(modifier = Modifier.height(24.dp))
            }

            val phone by signUpViewModel.phone.collectAsState()

            CustomButton(
                text = "Next",
                enabled = email.isNotBlank(),
                onClick = {
                    isSubmitted = true
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        if (phone.isNotBlank()) {
                            navController.navigate("signup_password") // skip phone if already present
                        } else {
                            navController.navigate("signup_phone")
                        }
                    }
                }
            )


            Spacer(modifier = Modifier.weight(1f))
        }
    }
}