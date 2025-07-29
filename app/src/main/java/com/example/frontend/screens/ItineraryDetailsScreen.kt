package com.example.frontend.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontend.components.ReviewCard
import androidx.navigation.NavHostController
import androidx.compose.ui.text.buildAnnotatedString
import com.example.frontend.models.Itinerary
import com.example.frontend.models.ItineraryAccessibility
import com.example.frontend.viewmodels.SavedItinerariesViewModel
import com.example.frontend.viewmodels.ItineraryAccessibilityViewModel
import com.example.frontend.viewmodels.ItineraryViewModel
import com.example.frontend.viewmodels.ReviewViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter


@Composable
fun ItineraryDetailsScreen(
    navController: NavHostController,
    itineraryId: String,
    savedViewModel: SavedItinerariesViewModel,
    itineraryViewModel: ItineraryViewModel
) {

    val allItineraries by itineraryViewModel.itineraries.collectAsState()

    LaunchedEffect(itineraryId) {
        Log.d("ItineraryDetails", "Checking for itinerary: $itineraryId")
        itineraryViewModel.fetchItineraries()
    }

    LaunchedEffect(allItineraries) {
        Log.d("ItineraryDetails", "Available itineraries (${allItineraries.size}):")
        allItineraries.forEach {
            Log.d("ItineraryDetails", "- ${it.id} → ${it.title}")
        }
    }


    val reviewViewModel: ReviewViewModel = viewModel()
    val accessibilityViewModel: ItineraryAccessibilityViewModel = viewModel()

    val itinerary by remember(allItineraries, itineraryId) {
        derivedStateOf {
            allItineraries.find { it.id.toString() == itineraryId }
        }
    }

    val savedItineraries by savedViewModel.savedItineraries.collectAsState()
    val isSaved = remember(itinerary?.id, savedItineraries) {
        savedItineraries.any { it.itineraryId == itinerary?.id }
    }

    LaunchedEffect(Unit) {
        reviewViewModel.fetchAllReviews()
    }

    val allReviews by reviewViewModel.reviews.collectAsState()

    LaunchedEffect(allReviews) {
        Log.d("ItineraryDetails", "Fetched ${allReviews.size} reviews")
    }

    LaunchedEffect(itinerary) {
        Log.d("ItineraryDetails", "Current itinerary ID: ${itinerary?.id}")
    }


    LaunchedEffect(allReviews) {
        allReviews.forEach { review ->
            Log.d("ItineraryDetails", "Review: id=${review.id}, itineraryId=${review.itineraryId}")
        }
    }


    val filteredReviews = itinerary?.id?.toString()?.let { id ->
        allReviews.filter { it.itineraryId?.toString() == id }
    } ?: emptyList()


    LaunchedEffect(filteredReviews) {
        Log.d("ItineraryDetails", "Filtered reviews count: ${filteredReviews.size}")
    }


    val accessibilityLinks by accessibilityViewModel.accessibilityLinks.collectAsState()
    val featureLabels = mapOf(
        UUID.fromString("00000000-0000-0000-0000-000000000001") to "Wheelchair Accessible ✅",
        UUID.fromString("00000000-0000-0000-0000-000000000002") to "Hearing Aid Support ✅",
        UUID.fromString("00000000-0000-0000-0000-000000000003") to "Elevator ✅"
    )

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = { HomeTopBar(navController) },
        bottomBar = { BottomNavBar(navController, selectedScreen = "search") },
        snackbarHost = { SnackbarHost(snackbarHostState) }
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                item {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = itinerary?.title ?: "Loading...",
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                modifier = Modifier.weight(1f)
                            )

                            itinerary?.id?.let {
                                Log.d("ItineraryDetails", "Bookmark clicked for $it")
                            }

                            IconButton(
                                onClick = {
                                    itinerary?.id?.let { id ->
                                        coroutineScope.launch {
                                            if (isSaved) {
                                                savedViewModel.removeItinerary(id)
                                                snackbarHostState.showSnackbar("Removed from Saved Itineraries")
                                            } else {
                                                savedViewModel.saveItinerary(id)
                                                snackbarHostState.showSnackbar("Saved to Itineraries")
                                            }
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                    contentDescription = "Save Itinerary",
                                    tint = Color.Black
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        val painter = rememberAsyncImagePainter(itinerary?.imageUrl ?: "")

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.LightGray)
                        ) {
                            Image(
                                painter = painter,
                                contentDescription = "Itinerary Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }


                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = buildAnnotatedString {
                                append("This itinerary lasts approximately ")
                                boldText("2-3 hours")
                                append(", offering a fully accessible experience. ")
                                append("Visitors can rely on ")
                                boldText("accessible taxis and metro services")
                                append(" to reach key destinations. The itinerary also provides ")
                                boldText("free cancellation")
                                append(" up to ")
                                boldText("24 hours before the trip")
                                append(" if needed.\n\n")

                                append("For those requiring additional accessibility options, ")
                                boldText("alternative routes")
                                append(" include a ")
                                boldText("wheelchair-accessible pathway via Rue de Rivoli")
                                append(", ")
                                boldText("elevator access at the museum")
                                append(", and ")
                                boldText("nearby tram services with wheelchair access")
                                append(".")
                            },
                            fontSize = 16.sp,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        val itineraryIdToSet = itinerary?.id

                        Button(
                            onClick = {
                                itineraryIdToSet?.let { id ->
                                    savedViewModel.setAsNextPlan(id)
                                    coroutineScope.launch {
                                        if (isSaved) {
                                            snackbarHostState.showSnackbar("Set as Next Plan")
                                        } else {
                                            snackbarHostState.showSnackbar("Set as Next Plan (not saved)")
                                        }
                                    }
                                } ?: run {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Unable to set plan: Itinerary not loaded")
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFF9AA6B2),
                                contentColor = Color.Black
                            )
                        ) {
                            Text("Set as Next Plan", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                item {
                    AccessibilityOverviewSection(accessibilityLinks, featureLabels)
                }

                item {
                    DetailsNavigationRow(navController, itinerary)
                }

                item {
                    Column(modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)) {
                        Text(
                            "Reviews",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                }

                // Show fetched reviews
                items(filteredReviews, key = { it.id }) { review ->
                    Log.d("ItineraryDetails", "Rendering review for itinerary ${review.itineraryId}")
                    ReviewCard(review, navController, showItineraryButton = false)
                }

                // Write Review Button comes last
                item {
                    itinerary?.id?.let { id ->
                        Button(
                            onClick = { navController.navigate("write_review/$id") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .padding(vertical = 8.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFF9AA6B2),
                                contentColor = Color.Black
                            )
                        ) {
                            Icon(
                                Icons.Filled.Star,
                                contentDescription = "Write Review",
                                tint = Color.Black
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Write a Review", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

fun AnnotatedString.Builder.boldText(text: String) {
    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
        append(text)
    }
}

@Composable
fun AccessibilityOverviewSection(
    accessibilityLinks: List<ItineraryAccessibility>,
    featureLabels: Map<UUID, String>
) {
    var isExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Accessibility Overview",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = if (isExpanded) Icons.Filled.KeyboardArrowDown else Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Expand/Collapse",
            modifier = Modifier.size(24.dp)
        )
    }

    if (isExpanded) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            accessibilityLinks.forEach { link ->
                val label = featureLabels[link.featureId] ?: "Unknown Feature"
                val backgroundColor =
                    if (label.contains("✅")) Color(0xFFDFF6DD) else Color(0xFFFFD6D6)
                AccessibilityTag(label, backgroundColor)
            }
        }
    }
}

@Composable
fun AccessibilityTag(label: String, backgroundColor: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        backgroundColor = backgroundColor,
        shape = RoundedCornerShape(8.dp),
        elevation = 2.dp
    ) {
        Box(
            modifier = Modifier.padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
    }
}

@Composable
fun DetailsNavigationRow(navController: NavHostController, itinerary: Itinerary?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                itinerary?.id?.let {
                    navController.navigate("itinerary_steps/$it")
                }
            }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Details",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Navigate to Details",
            modifier = Modifier.size(24.dp)
        )
    }
}
