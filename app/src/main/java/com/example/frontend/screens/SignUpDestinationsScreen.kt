/*
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
import com.example.frontend.components.CustomChipGroup
import com.example.frontend.components.SignUpProgressBar
import com.example.frontend.viewmodels.CountryViewModel
import com.example.frontend.viewmodels.DestinationViewModel
import com.example.frontend.viewmodels.SignUpViewModel

@Composable
fun SignUpDestinationsScreen(
    navController: NavHostController,
    destinationViewModel: DestinationViewModel = viewModel(),
) {
    val parentEntry = remember(navController) { navController.getBackStackEntry("signup_flow") }
    val signUpViewModel: SignUpViewModel = viewModel(parentEntry)

    val destinations by destinationViewModel.destinations.collectAsState()
    val selectedDestinations by signUpViewModel.selectedDestinations.collectAsState()
    val selectedPlaces by signUpViewModel.selectedPlaces.collectAsState()

    val countryViewModel: CountryViewModel = viewModel()
    val countries by countryViewModel.countries.collectAsState()
    val countryNameToIdMap = countries.associate { it.name to it.id }

    val selectedCountry = remember(selectedDestinations) {
        selectedDestinations.firstOrNull()
    }

    LaunchedEffect(selectedCountry) {
        Log.d("SignUpDestinations", "Fetched selectedDestinations: $selectedDestinations")
        Log.d("SignUpDestinations", "Selected country: $selectedCountry")
    }

    val destinationsForCountry = destinations.filter { it.countryName == selectedCountry && it.available }
    val destinationOptions = destinationsForCountry.map { it.name }

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
            SignUpProgressBar(currentStep = 6, totalSteps = 6)
            BackButton(navController)

            Spacer(modifier = Modifier.weight(0.5f))

            Text(
                text = "Select up to three destinations you wish to visit",
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Spacer(modifier = Modifier.weight(0.4f))

            if (destinationOptions.isEmpty()) {
                Text(
                    text = "No destinations available for the selected city yet.",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 24.dp)
                )
            } else {
                CustomChipGroup(
                    options = destinationOptions,
                    selectedOptions = selectedPlaces,
                    onSelectionChanged = { newSelection ->
                        if (newSelection.size <= 3) {
                            signUpViewModel.updateSelectedPlaces(newSelection)
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            CustomButton(
                text = "Next",
                enabled = selectedPlaces.isNotEmpty(),
                onClick = {
                    signUpViewModel.submitSignup(countryNameToIdMap)
                    navController.navigate("home")
                },
                fontWeight = if (selectedPlaces.isNotEmpty()) FontWeight.Bold else FontWeight.Medium
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
*/
