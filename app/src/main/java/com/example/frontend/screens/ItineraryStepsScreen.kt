package com.example.frontend.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.frontend.components.BackButton
import com.example.frontend.components.MapScreen
import com.example.frontend.viewmodels.ItineraryStepViewModel
import com.google.android.gms.maps.model.LatLng
import java.util.UUID
import androidx.compose.ui.Alignment



@Composable
fun ItineraryStepsScreen(navController: NavHostController, itineraryIdStr: String) {
    val viewModel: ItineraryStepViewModel = viewModel()
    val steps by viewModel.steps.collectAsState()

    val itineraryId = remember(itineraryIdStr) {
        try {
            UUID.fromString(itineraryIdStr).also {
                Log.d("ItineraryStepsScreen", "Parsed UUID: $it")
            }
        } catch (e: IllegalArgumentException) {
            Log.e("ItineraryStepsScreen", "Invalid UUID: $itineraryIdStr", e)
            null
        }
    }


    LaunchedEffect(itineraryId) {
        itineraryId?.let {
            Log.d("ItineraryStepsScreen", "Calling fetchSteps with $it")
            viewModel.fetchSteps(it)
        }
    }


    var selectedStep by remember { mutableIntStateOf(0) }

    val stepLocations = steps.map { LatLng(it.latitude.toDouble(), it.longitude.toDouble()) }

    Scaffold(
        topBar = { HomeTopBar(navController) },
        bottomBar = { BottomNavBar(navController, selectedScreen = "search") },
    ) { padding ->

        val error by viewModel.error.collectAsState()

        if (steps.isEmpty() && error != null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No steps available for this itinerary.", color = Color.Gray)
            }
            return@Scaffold
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFF8FAFC), Color(0xFFD9EAFD))
                    )
                )
                .padding(padding)
        ) {
            BackButton(navController)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                MapScreen(stepLocations, selectedStep)
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                itemsIndexed(steps) { index, step ->
                    StepItem(index, selectedStep, step.title, step.description) {
                        selectedStep = index
                    }
                }
            }
        }
    }
}

@Composable
fun StepItem(index: Int, selectedStep: Int, title: String, description: String, onStepClick: () -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                onStepClick()
                isExpanded = !isExpanded
            },
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
        backgroundColor = if (index == selectedStep) Color(0xFF9AA6B2) else Color.White
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "Step ${index + 1}: $title",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
        }
    }
}