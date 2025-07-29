package com.example.frontend.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.frontend.components.RecommendedDestinationCard
import com.example.frontend.viewmodels.SavedItinerariesViewModel
import com.example.frontend.viewmodels.ItineraryViewModel
import java.util.Locale

@Composable
fun SavedItinerariesScreen(
    navController: NavHostController,
    savedViewModel: SavedItinerariesViewModel,
    itineraryViewModel: ItineraryViewModel = viewModel()
) {
    val savedItineraries by savedViewModel.savedItineraries.collectAsState()
    val itineraries by itineraryViewModel.itineraries.collectAsState()

    // Explicitly fetch itineraries from backend
    LaunchedEffect(Unit) {
        itineraryViewModel.fetchItineraries()
    }

    Scaffold(
        topBar = { HomeTopBar(navController) },
        bottomBar = { BottomNavBar(navController, selectedScreen = "saved_itineraries") }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFF8FAFC), Color(0xFFD9EAFD), Color(0xFFBCCCDC))
                    )
                )
                .padding(padding)
                .padding(horizontal = 16.dp),
            color = Color.Transparent
        ) {
            if (savedItineraries.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No itineraries saved yet!",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(savedItineraries) { saved ->
                        val itinerary = itineraries.find { it.id == saved.itineraryId }

                        if (itinerary != null) {
                            val title = itinerary.title ?: "Untitled"
                            val location = itinerary.destinationName ?: "Coming soon"
                            val rating = itinerary.rating?.let {
                                String.format(Locale.US, "%.1f", it.toDouble()).toDouble()
                            } ?: 0.0
                            val price = itinerary.price?.toInt()?.toString() ?: "Free"
                            val duration = "${itinerary.duration ?: "?"} hours"

                            RecommendedDestinationCard(
                                title = title,
                                location = location,
                                rating = rating,
                                price = price,
                                duration = duration,
                                people = 2,
                                imageUrl = itinerary.imageUrl,
                                accessibilityFeatures = listOf(
                                    "Wheelchair Accessible",
                                    "Elevator Access"
                                ),
                                onClick = {
                                    navController.navigate("itinerary_details/${itinerary.id}")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
