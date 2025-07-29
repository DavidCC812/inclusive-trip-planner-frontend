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
import com.example.frontend.components.BackButton
import com.example.frontend.components.CustomButton
import com.example.frontend.components.CustomInputField
import com.example.frontend.components.SignUpProgressBar
import com.example.frontend.viewmodels.SignUpViewModel

@Composable
fun SignUpPasswordScreen(
    navController: NavHostController,
) {
    val parentEntry = remember(navController) { navController.getBackStackEntry("signup_flow") }
    val signUpViewModel: SignUpViewModel = viewModel(parentEntry)

    var confirmPassword by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) }

    val password by signUpViewModel.password.collectAsState()

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
            SignUpProgressBar(currentStep = 3, totalSteps = 5)
            BackButton(navController)

            Spacer(modifier = Modifier.weight(0.5f))

            Text(
                text = "Create Your Password",
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Spacer(modifier = Modifier.weight(0.4f))

            CustomInputField(
                value = password,
                onValueChange = { signUpViewModel.updatePassword(it) },
                label = "Password",
                isError = isSubmitted && (password.length < 6 || !password.any { it.isDigit() }),
                keyboardType = KeyboardType.Password,
                backgroundAlpha = 0.25f,
                textStyle = LocalTextStyle.current.copy(fontSize = 20.sp)
            )

            if (isSubmitted && (password.length < 6 || !password.any { it.isDigit() })) {
                Text(
                    text = "Password must be at least 6 characters and contain a number",
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
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Confirm Password",
                isError = isSubmitted && confirmPassword != password,
                keyboardType = KeyboardType.Password,
                backgroundAlpha = 0.25f,
                textStyle = LocalTextStyle.current.copy(fontSize = 20.sp)
            )

            if (isSubmitted && confirmPassword != password) {
                Text(
                    text = "Passwords do not match",
                    color = Color.Red,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp, bottom = 10.dp),
                    textAlign = TextAlign.Start
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            CustomButton(
                text = "Next",
                enabled = password.length >= 6 && password.any { it.isDigit() } && password == confirmPassword,
                onClick = {
                    isSubmitted = true
                    if (password.length >= 6 && password.any { it.isDigit() } && password == confirmPassword) {
                        navController.navigate("signup_accessibility")
                    }
                },
                fontWeight = if (password.length >= 6 && password.any { it.isDigit() } && password == confirmPassword) FontWeight.Bold else FontWeight.Medium
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}