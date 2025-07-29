package com.example.frontend.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.frontend.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.ColorFilter
import java.util.Locale
import androidx.navigation.NavHostController
import com.example.frontend.components.CustomButton
import com.example.frontend.components.RecommendedDestinationCard
import com.example.frontend.viewmodels.SavedItinerariesViewModel
import com.example.frontend.viewmodels.ItineraryViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    savedViewModel: SavedItinerariesViewModel,
    itineraryViewModel: ItineraryViewModel
) {

    Scaffold(
        topBar = { HomeTopBar(navController) },
        bottomBar = { BottomNavBar(navController, selectedScreen = "home") },
        backgroundColor = Color.Transparent
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
                .padding(horizontal = 24.dp),
            color = Color.Transparent
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                NextPlanSection(navController, savedViewModel, itineraryViewModel)
                Spacer(modifier = Modifier.height(16.dp))

                RecommendedDestinationsSection(navController, itineraryViewModel) // ✅ passed here
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}


@Composable
fun HomeTopBar(navController: NavHostController) {
    TopAppBar(
        backgroundColor = Color(0xFF9AA6B2),
        elevation = 4.dp,
        modifier = Modifier.height(56.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "App Logo",
                colorFilter = ColorFilter.tint(Color.Black),
                modifier = Modifier
                    .height(48.dp)
            )

            // Notifications Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = { navController.navigate("notifications") }
                ) {
                    Box(contentAlignment = Alignment.TopEnd) {
                        Icon(
                            imageVector = Icons.Filled.Notifications,
                            contentDescription = "Notifications",
                            tint = Color.Black,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun BottomNavBar(navController: NavHostController, selectedScreen: String) {
    BottomAppBar(
        backgroundColor = Color(0xFF9AA6B2),
        elevation = 8.dp,
        modifier = Modifier.height(60.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavButton(
                icon = Icons.Filled.Home,
                selected = selectedScreen == "home",
                onClick = { navController.navigate("home") }
            )

            BottomNavButton(
                iconResId = android.R.drawable.ic_menu_search,
                selected = selectedScreen == "search",
                onClick = { navController.navigate("search") }
            )

            BottomNavButton(
                icon = Icons.Filled.Bookmark,
                selected = selectedScreen == "itinerary",
                onClick = { navController.navigate("saved_itineraries") }
            )
            BottomNavButton(
                icon = Icons.Filled.Person,
                selected = selectedScreen == "profile",
                onClick = { navController.navigate("profile") }
            )
        }
    }
}

@Composable
fun BottomNavButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    iconResId: Int? = null,
    selected: Boolean,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) Color.Black else Color(0xFF5F6B78),
                modifier = Modifier.size(32.dp)
            )
        } else if (iconResId != null) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                tint = if (selected) Color.Black else Color(0xFF5F6B78),
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun NextPlanSection(
    navController: NavHostController,
    savedViewModel: SavedItinerariesViewModel,
    itineraryViewModel: ItineraryViewModel
) {
    val nextPlan = savedViewModel.nextPlan.collectAsState().value
    val itinerary by itineraryViewModel
        .getItineraryById(nextPlan?.itineraryId?.toString() ?: "")
        .collectAsState(initial = null)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Next Plan",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        val resolvedItinerary = itinerary

        LaunchedEffect(nextPlan) {
            Log.d("NextPlanSection", "nextPlan: $nextPlan")
            Log.d("NextPlanSection", "resolvedItinerary: $resolvedItinerary")
        }

        if (nextPlan == null || resolvedItinerary == null) {
            Text(
                text = "You have no reserved plans yet.",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            CustomButton(
                text = "Find Itineraries",
                onClick = { navController.navigate("search") },
                backgroundColor = Color(0xFF9AA6B2),
                textColor = Color.Black,
                buttonHeight = 40.dp,
                modifier = Modifier.fillMaxWidth(0.85f)
            )
        } else {
            val title = resolvedItinerary.title ?: "Untitled"
            val location = resolvedItinerary.destinationName ?: "Coming soon"
            val rating = resolvedItinerary.rating?.let {
                String.format(Locale.US, "%.1f", it.toDouble()).toDouble()
            } ?: 0.0
            val price = resolvedItinerary.price?.toInt()?.toString() ?: "Free"
            val duration = "${resolvedItinerary.duration ?: "?"} hours"

            RecommendedDestinationCard(
                title = title,
                location = location,
                rating = rating,
                price = price,
                duration = duration,
                people = 2,
                imageUrl = itinerary?.imageUrl,
                accessibilityFeatures = listOf("Wheelchair Accessible", "Braille Available"),
                onClick = {
                    navController.navigate("itinerary_details/${resolvedItinerary.id}")
                }
            )
        }
    }
}

@Composable
fun RecommendedDestinationsSection(
    navController: NavHostController,
    viewModel: ItineraryViewModel
) {
    val itineraries by viewModel.itineraries.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Text(
            text = "Recommended Destinations",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Split itineraries into chunks of 3 to create rows
        val rows = itineraries.chunked(3)

        rows.forEach { rowItineraries ->
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp), // Space between rows
                contentPadding = PaddingValues(start = 4.dp, end = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(rowItineraries) { itinerary ->
                    val formattedRating = itinerary.rating?.let {
                        String.format(Locale.US, "%.1f", it.toDouble()).toDouble()
                    } ?: 0.0

                    val priceString = itinerary.price?.toInt()?.toString() ?: "Free"
                    val durationText = "${itinerary.duration ?: "?"} hours"

                    RecommendedDestinationCard(
                        title = itinerary.title,
                        location = itinerary.destinationName ?: "Coming soon",
                        rating = formattedRating,
                        price = priceString,
                        duration = durationText,
                        people = 2,
                        imageUrl = itinerary.imageUrl,
                        accessibilityFeatures = listOf("Wheelchair Accessible", "Braille Available"),
                        onClick = {
                            navController.navigate("itinerary_details/${itinerary.id}")
                        }
                    )
                }
            }
        }
    }
}