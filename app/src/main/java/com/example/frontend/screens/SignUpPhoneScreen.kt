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
import com.example.frontend.components.PhoneInputField
import com.example.frontend.components.SignUpProgressBar
import com.example.frontend.viewmodels.SignUpViewModel

@Composable
fun SignUpPhoneScreen(
    navController: NavHostController,
) {

    val parentEntry = remember(navController) { navController.getBackStackEntry("signup_flow") }
    val signUpViewModel: SignUpViewModel = viewModel(parentEntry)

    var isSubmitted by remember { mutableStateOf(false) }

    val phoneNumber by signUpViewModel.phone.collectAsState()

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
                text = "Enter your phone number",
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Spacer(modifier = Modifier.weight(0.4f))

            PhoneInputField(
                value = phoneNumber,
                onValueChange = { signUpViewModel.updatePhone(it) },
                label = "Phone Number",
                isError = isSubmitted && phoneNumber.length != 10,
            )

            if (isSubmitted && phoneNumber.length != 10) {
                Text(
                    text = "Enter a valid 10-digit phone number",
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

            Spacer(modifier = Modifier.height(20.dp))

            CustomButton(
                text = "Next",
                enabled = phoneNumber.length == 10,
                onClick = {
                    isSubmitted = true
                    if (phoneNumber.length == 10) {
                        navController.navigate("signup_password")
                    }
                }
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}