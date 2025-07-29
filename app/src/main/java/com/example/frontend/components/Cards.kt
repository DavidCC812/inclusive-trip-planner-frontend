package com.example.frontend.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.models.Review
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material3.CardDefaults
import androidx.navigation.NavHostController
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.clip
import coil.compose.rememberAsyncImagePainter
import java.util.UUID


@Composable
fun RecommendedDestinationCard(
    title: String,
    location: String,
    rating: Double,
    price: String,
    duration: String,
    people: Int,
    imageUrl: String?,
    accessibilityFeatures: List<String>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .width(220.dp)
            .padding(8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            val painter = rememberAsyncImagePainter(imageUrl ?: "")

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
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

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(text = location, fontSize = 12.sp, color = Color.Gray)
                }
                Text(
                    text = "⭐ $rating",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFA500),
                    textAlign = TextAlign.End
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "€$price",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_recent_history),
                    contentDescription = "Duration",
                    tint = Color.Black,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "$duration", fontSize = 12.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_myplaces),
                    contentDescription = "People",
                    tint = Color.Black,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "$people people", fontSize = 12.sp, color = Color.DarkGray)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Accessibility,
                    contentDescription = "Accessibility Features",
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Accessibility Features",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = LinearOutSlowInEasing
                            )
                        )
                        .padding(8.dp)
                ) {
                    accessibilityFeatures.forEach { feature ->
                        Text(text = "• $feature", fontSize = 12.sp, color = Color.Black)
                    }
                }
            }
        }
    }
}

@Composable
fun SearchDestinationCard(
    itineraryId: UUID,
    name: String,
    rating: Double,
    price: String,
    duration: String,
    people: Int,
    imageUrl: String?,
    accessibilityFeatures: List<String>,
    navController: NavHostController
) {
    androidx.compose.material.Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 12.dp)
            .clickable { navController.navigate("itinerary_details/$itineraryId") },
        elevation = 4.dp
    ) {
        Box(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val painter = rememberAsyncImagePainter(imageUrl ?: "")

                Box(
                    modifier = Modifier
                        .size(60.dp)
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


                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    androidx.compose.material.Text(
                        text = name,
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    accessibilityFeatures.forEach { feature ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            androidx.compose.material.Icon(
                                imageVector = Icons.Filled.Accessibility,
                                contentDescription = "Accessibility",
                                tint = Color.DarkGray,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            androidx.compose.material.Text(
                                text = feature,
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        androidx.compose.material.Icon(
                            imageVector = Icons.Outlined.AccessTime,
                            contentDescription = "Duration",
                            tint = Color.DarkGray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        androidx.compose.material.Text(
                            text = duration,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        androidx.compose.material.Icon(
                            imageVector = Icons.Outlined.Group,
                            contentDescription = "People",
                            tint = Color.DarkGray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        androidx.compose.material.Text(
                            text = "$people people",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd),
                verticalAlignment = Alignment.CenterVertically
            ) {
                androidx.compose.material.Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Rating",
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                androidx.compose.material.Text(
                    text = rating.toString(),
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold
                )
            }

            androidx.compose.material.Text(
                text = "€$price",
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}

@Composable
fun ReviewCard(review: Review, navController: NavHostController, showItineraryButton: Boolean) {
    androidx.compose.material.Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(12.dp),
        backgroundColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                androidx.compose.material.Text(
                    "Anonymous",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                val formattedDate = try {
                    LocalDateTime.parse(review.createdAt)
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                } catch (e: Exception) {
                    review.createdAt
                }

                androidx.compose.material.Text(
                    formattedDate,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row {
                repeat(review.rating.toInt()) {
                    androidx.compose.material.Icon(
                        Icons.Filled.Star,
                        contentDescription = "Star",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            androidx.compose.material.Text(review.comment, fontSize = 14.sp, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(8.dp))
            androidx.compose.material.Divider(color = Color.LightGray, thickness = 1.dp)
            Spacer(modifier = Modifier.height(8.dp))
            if (showItineraryButton) {
                if (review.itineraryId != null) {
                    androidx.compose.material.Button(
                        onClick = {
                            Log.d("ReviewCard", "Navigating to itinerary ID: ${review.itineraryId}")
                            navController.navigate("itinerary_details/${review.itineraryId}")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF9AA6B2),
                            contentColor = Color.Black
                        )
                    ) {
                        androidx.compose.material.Text("View Itinerary", fontSize = 16.sp)
                    }
                } else {
                    Log.w("ReviewCard", "itineraryId is null — cannot navigate to details screen")
                }
            }
        }
    }
}
