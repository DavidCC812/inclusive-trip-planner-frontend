package com.example.frontend.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.frontend.components.SearchBar
import com.example.frontend.components.SearchDestinationCard
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontend.viewmodels.ItineraryViewModel
import java.util.*
import java.text.SimpleDateFormat

@Composable
fun SearchScreen(navController: NavHostController, itineraryViewModel: ItineraryViewModel) {
    var selectedFilter by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    var dateRange by remember { mutableStateOf(getCurrentDateRange()) }
    var searchTriggered by remember { mutableStateOf(false) }

    val accessibilityFilters =
        listOf("All", "Wheelchair Accessible", "Braille Available", "Hearing Aid")


    Scaffold(
        topBar = { HomeTopBar(navController) },
        bottomBar = { BottomNavBar(navController, selectedScreen = "search") }
    ) { padding ->
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
                .padding(horizontal = 16.dp),
            color = Color.Transparent
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Text(
                    text = "Search Destinations",
                    fontSize = 22.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 16.dp, start = 8.dp, bottom = 8.dp)
                )

                SearchBar(
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it },
                    selectedFilter = selectedFilter,
                    onFilterChange = { selectedFilter = it },
                    accessibilityFilters = accessibilityFilters,
                    selectedDates = dateRange,
                    onDateChange = { dateRange = it },
                    onSearch = { searchTriggered = true }
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (searchTriggered) {
                    SearchResultsList(
                        searchQuery,
                        selectedFilter,
                        navController,
                        itineraryViewModel
                    )
                } else {
                    Text(
                        text = "Use the search bar to find destinations.",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SearchResultsList(
    searchQuery: String,
    selectedFilter: String,
    navController: NavHostController,
    itineraryViewModel: ItineraryViewModel
) {
    val itineraries by itineraryViewModel.itineraries.collectAsState()

    val filteredItineraries = itineraries
        .filter { itinerary ->
            itinerary.title.contains(searchQuery, ignoreCase = true) ||
                    (itinerary.destinationName?.contains(searchQuery, ignoreCase = true) == true)
        }
        .filter {
            selectedFilter == "All" ||
                    listOf("Wheelchair Accessible", "Braille Available", "Hearing Aid").contains(selectedFilter)
        }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(filteredItineraries) { itinerary ->
            val formattedRating = String.format(Locale.US, "%.1f", (itinerary.rating ?: 0f).toDouble()).toDouble()
            val formattedPrice = itinerary.price?.toInt()?.toString() ?: "Free"

            SearchDestinationCard(
                itineraryId = itinerary.id,
                name = itinerary.title,
                rating = formattedRating,
                price = formattedPrice,
                duration = "${itinerary.duration ?: "?"} hours",
                people = 2,
                imageUrl = itinerary.imageUrl,
                accessibilityFeatures = listOf("Wheelchair Accessible", "Braille Available"),
                navController = navController
            )
        }
    }
}

fun getCurrentDateRange(): String {
    val sdf = SimpleDateFormat("MMM dd", Locale.getDefault())
    val today = sdf.format(Date())
    val nextWeek = sdf.format(Calendar.getInstance().apply { add(Calendar.DATE, 7) }.time)
    return "$today - $nextWeek"
}
