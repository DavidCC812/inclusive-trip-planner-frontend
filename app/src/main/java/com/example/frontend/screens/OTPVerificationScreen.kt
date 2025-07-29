package com.example.frontend.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.frontend.R
import com.example.frontend.components.CustomButton
import com.example.frontend.viewmodels.SignUpViewModel

@Composable
fun OTPVerificationScreen(
    navController: NavHostController,
    verificationType: String,
    identifier: String
) {
    var otpValues by remember { mutableStateOf(List(6) { "" }) }
    var otpError by remember { mutableStateOf("") }

    val parentEntry = remember(navController) {
        try {
            navController.getBackStackEntry("signup_flow")
        } catch (e: Exception) {
            null
        }
    }
    val signUpViewModel: SignUpViewModel? = parentEntry?.let { viewModel(it) }

    LaunchedEffect(Unit) {
        signUpViewModel?.email?.value?.let {
            Log.d("OTPVerification", "SignUpViewModel email on load: $it")
        }
    }


    Surface(
        modifier = Modifier.fillMaxSize().background(
            Brush.verticalGradient(
                colors = listOf(Color(0xFFF8FAFC), Color(0xFFD9EAFD), Color(0xFFBCCCDC))
            )
        ).padding(horizontal = 24.dp),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(top = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo), // or whatever your filename is
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(160.dp)
            )

            Spacer(modifier = Modifier.height(100.dp))

            Text(
                text = "Verification",
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Enter the OTP sent to your $verificationType",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                otpValues.forEachIndexed { index, value ->
                    OutlinedTextField(
                        value = value,
                        onValueChange = {
                            if (it.length <= 1) otpValues =
                                otpValues.toMutableList().apply { this[index] = it }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.width(50.dp).height(60.dp),
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 22.sp,
                            textAlign = TextAlign.Center
                        ),
                        singleLine = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            CustomButton(
                text = "Verify",
                enabled = otpValues.all { it.isNotEmpty() },
                onClick = {
                    when (verificationType) {
                        "email" -> {
                            signUpViewModel?.updateEmail(identifier)
                            Log.d("OTPVerification", "Email set to: ${signUpViewModel?.email?.value}")
                        }
                        "phone" -> {
                            signUpViewModel?.updatePhone(identifier)
                            Log.d("OTPVerification", "Phone set to: ${signUpViewModel?.phone?.value}")
                        }
                    }
                    navController.navigate("signup_name") {
                        popUpTo("signup_flow") { inclusive = false }
                    }
                }
            )
        }
    }
}