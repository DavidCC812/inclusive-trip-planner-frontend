package com.example.frontend.screens

import android.util.Log
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.frontend.R
import com.example.frontend.components.CustomButton
import com.example.frontend.components.CustomInputField
import com.example.frontend.viewmodels.SignUpViewModel
import com.example.frontend.viewmodels.UserViewModel

@Composable
fun EmailScreen(navController: NavHostController, viewModel: UserViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }

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
            Log.d("EmailScreen", "SignUpViewModel already has email: $it")
        }
    }

    val isLoading by viewModel.isLoading.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize().background(
            Brush.verticalGradient(
                listOf(Color(0xFFF8FAFC), Color(0xFFD9EAFD), Color(0xFFBCCCDC))
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

            Spacer(modifier = Modifier.height(210.dp))

            CustomInputField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = ""
                },
                label = "Enter your email",
                isError = emailError.isNotEmpty(),
                keyboardType = KeyboardType.Email,
                textStyle = LocalTextStyle.current.copy(fontSize = 20.sp, color = Color.Black)
            )

            if (emailError.isNotEmpty()) {
                Text(
                    text = emailError,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth().padding(top = 6.dp, bottom = 10.dp),
                    textAlign = TextAlign.Start
                )
            } else {
                Spacer(modifier = Modifier.height(16.dp))
            }

            CustomButton(
                text = if (isLoading) "Checking..." else "Continue",
                enabled = email.isNotEmpty() && !isLoading,
                onClick = {
                    if (email.isBlank()) {
                        emailError = "Email is required"
                    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        emailError = "Enter a valid email address"
                    } else {
                        viewModel.fetchUserByEmail(email) { user ->
                            if (user != null) {
                                navController.navigate("login_with_password/${user.email}")
                            } else {
                                signUpViewModel?.updateEmail(email)
                                Log.d("EmailScreen", "Email passed to signup flow: $email")
                                navController.navigate("otp_verification/email/$email")
                            }
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}