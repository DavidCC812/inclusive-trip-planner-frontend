package com.example.frontend.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.navigation.NavHostController
import androidx.compose.ui.platform.LocalConfiguration
import com.example.frontend.components.BackButton

@Composable
fun PrivacyPolicyScreen(navController: NavHostController) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp

    // Adaptive font sizes
    val titleFontSize = if (screenWidthDp > 600) 32.sp else 28.sp
    val contentFontSize = if (screenWidthDp > 600) 18.sp else 16.sp

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
            Spacer(modifier = Modifier.height(48.dp))
            BackButton(navController)


            // Title
            Text(
                text = "Privacy Policy",
                fontSize = titleFontSize,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Scrollable Content
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = """
                        Your privacy is important to us. This Privacy Policy outlines how we handle your personal information when using our application.
                        
                        1. **Data Collection:** We collect data necessary to provide our services, such as email addresses, preferences, and interactions.
                        2. **Usage of Information:** Your data is used to enhance your experience, provide recommendations, and ensure app security.
                        3. **Data Sharing:** We do not sell or share your personal information with third parties, except where legally required.
                        4. **Security Measures:** We take steps to protect your data from unauthorized access and breaches.
                        5. **Your Rights:** You can request access, updates, or deletion of your data at any time.
                        
                        For further details, please contact our support team.
                    """.trimIndent(),
                    fontSize = contentFontSize,
                    color = Color.Black.copy(alpha = 0.85f),
                    lineHeight = 22.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}