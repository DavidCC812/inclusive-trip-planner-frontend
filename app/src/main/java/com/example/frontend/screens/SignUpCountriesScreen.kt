package com.example.frontend.screens

import android.util.Log
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
import com.example.frontend.viewmodels.CountryViewModel
import com.example.frontend.viewmodels.SignUpViewModel

@Composable
fun SignUpCountriesScreen(
    navController: NavHostController,
    countryViewModel: CountryViewModel = viewModel(),
) {
    val parentEntry = remember(navController) { navController.getBackStackEntry("signup_flow") }
    val signUpViewModel: SignUpViewModel = viewModel(parentEntry)

    val countries by countryViewModel.countries.collectAsState()
    val isLoading by countryViewModel.isLoading.collectAsState()
    val selectedDestinations by signUpViewModel.selectedDestinations.collectAsState()

    var selectedOptions by remember { mutableStateOf(selectedDestinations) }

    val availableCountries = countries.filter { it.available }
    val comingSoonCountries = countries.filter { !it.available }
    val allOptions = availableCountries + comingSoonCountries
    val countryNameToIdMap = countries.associate { it.name to it.id }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFF8FAFC), Color(0xFFD9EAFD), Color(0xFFBCCCDC))
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
            SignUpProgressBar(currentStep = 5, totalSteps = 5)
            BackButton(navController)

            Spacer(modifier = Modifier.weight(0.5f))
            Text(
                text = "Select the cities you wish to visit",
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Spacer(modifier = Modifier.weight(0.4f))

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(top = 32.dp))
            } else {
                CustomChipGrid(
                    options = allOptions.map { it.name },
                    selectedOptions = selectedOptions,
                    onSelectionChanged = { updatedSelection ->
                        selectedOptions = updatedSelection
                        signUpViewModel.updateSelectedDestinations(updatedSelection)
                    },
                    disabledOptions = comingSoonCountries.map { it.name }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            CustomButton(
                text = "Next",
                enabled = selectedOptions.isNotEmpty(),
                onClick = {
                    Log.d("SignUpCountries", "Submitting signup for: $selectedOptions")
                    signUpViewModel.updateSelectedDestinations(selectedOptions)
                    signUpViewModel.submitSignup(countryNameToIdMap)
                    navController.navigate("home")
                },
                fontWeight = if (selectedOptions.isNotEmpty()) FontWeight.Bold else FontWeight.Medium
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
