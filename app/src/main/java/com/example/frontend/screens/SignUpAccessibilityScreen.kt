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
import com.example.frontend.components.CustomChipGrid
import com.example.frontend.components.SignUpProgressBar
import com.example.frontend.viewmodels.AccessibilityFeatureViewModel
import com.example.frontend.viewmodels.SignUpViewModel

@Composable
fun SignUpAccessibilityScreen(
    navController: NavHostController,
    featureViewModel: AccessibilityFeatureViewModel = viewModel(),
) {
    val parentEntry = remember(navController) { navController.getBackStackEntry("signup_flow") }
    val signUpViewModel: SignUpViewModel = viewModel(parentEntry)

    val features by featureViewModel.features.collectAsState()
    val isLoading by featureViewModel.isLoading.collectAsState()
    val selectedFeatureIds by signUpViewModel.selectedAccessibilityFeatures.collectAsState()

    val options = features.map { it.name }
    val nameToIdMap = features.associateBy { it.name }

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
            SignUpProgressBar(currentStep = 4, totalSteps = 5)
            BackButton(navController)

            Spacer(modifier = Modifier.weight(0.5f))

            Text(
                text = "Select Accessibility Needs",
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Spacer(modifier = Modifier.weight(0.3f))

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(top = 32.dp))
            } else {
                CustomChipGrid(
                    options = options,
                    selectedOptions = selectedFeatureIds.mapNotNull { id -> features.find { it.id == id }?.name }.toSet(),
                    onSelectionChanged = { selectedNames ->
                        val updatedIds = selectedNames.mapNotNull { name -> nameToIdMap[name]?.id }.toSet()
                        signUpViewModel.updateSelectedAccessibilityFeatures(updatedIds)
                    },
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(25.dp))

            CustomButton(
                text = "Next",
                enabled = selectedFeatureIds.isNotEmpty(),
                onClick = {
                    navController.navigate("signup_countries")
                },
                fontWeight = if (selectedFeatureIds.isNotEmpty()) FontWeight.Bold else FontWeight.Medium
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
