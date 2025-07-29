package com.example.frontend.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.frontend.models.ReviewRequest
import com.example.frontend.viewmodels.ReviewViewModel
import java.util.UUID

@Composable
fun WriteAReviewScreen(
    navController: NavHostController,
    reviewViewModel: ReviewViewModel,
    userId: UUID,
    itineraryId: String
) {
    var userName by remember { mutableStateOf("") }
    var reviewText by remember { mutableStateOf("") }
    var rating by remember { mutableIntStateOf(0) }
    var anonymous by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = { HomeTopBar(navController) },
        bottomBar = { BottomNavBar(navController, selectedScreen = "profile") }
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
                .padding(padding)
                .padding(horizontal = 16.dp),
            color = Color.Transparent
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                // Review Form Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 6.dp,
                    shape = RoundedCornerShape(12.dp),
                    backgroundColor = Color.White
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        // Rating
                        Text("Select Rating", fontSize = 18.sp, color = Color.Black)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            for (i in 1..5) {
                                IconButton(onClick = { rating = i }) {
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = "Star $i",
                                        tint = if (i <= rating) Color(0xFFFFD700) else Color.Gray,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                            }
                        }

                        Divider(modifier = Modifier.padding(vertical = 12.dp))

                        // Name input (optional)
                        Text("Your Name (Optional)", fontSize = 18.sp, color = Color.Black)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = userName,
                            onValueChange = { userName = it },
                            label = { Text("Enter your name", color = Color.DarkGray) },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Color(0xFF9AA6B2),
                                unfocusedBorderColor = Color.Gray,
                                backgroundColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )

                        // Anonymous checkbox
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = anonymous,
                                onCheckedChange = { anonymous = it }
                            )
                            Text("Post as Anonymous", fontSize = 16.sp, color = Color.Black)
                        }

                        Divider(modifier = Modifier.padding(vertical = 12.dp))

                        // Review text input
                        Text("Write Your Review", fontSize = 18.sp, color = Color.Black)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = reviewText,
                            onValueChange = { reviewText = it },
                            label = { Text("Type your review", color = Color.DarkGray) },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Color(0xFF9AA6B2),
                                unfocusedBorderColor = Color.Gray,
                                backgroundColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                }

                errorMessage?.let {
                    Text(it, color = Color.Red, fontSize = 14.sp)
                }

                // Submit Button
                Button(
                    onClick = {
                        if (rating == 0) {
                            errorMessage = "Please select a rating."
                            return@Button
                        }
                        if (reviewText.isBlank()) {
                            errorMessage = "Review cannot be empty."
                            return@Button
                        }

                        val parsedItineraryId = try {
                            UUID.fromString(itineraryId)
                        } catch (e: IllegalArgumentException) {
                            errorMessage = "Invalid itinerary ID."
                            return@Button
                        }

                        val request = ReviewRequest(
                            userId = userId,
                            itineraryId = parsedItineraryId,
                            rating = rating.toFloat(),
                            comment = reviewText.trim()
                        )

                        reviewViewModel.postReview(
                            request,
                            onSuccess = {
                                errorMessage = null
                                navController.popBackStack()
                            },
                            onError = { error ->
                                errorMessage = error ?: "Something went wrong while posting your review."
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (rating > 0 && reviewText.isNotBlank())
                            Color(0xFF9AA6B2)
                        else Color.Gray,
                        contentColor = Color.White
                    ),
                    enabled = rating > 0 && reviewText.isNotBlank()
                ) {
                    Text("Submit Review", fontSize = 18.sp)
                }
            }
        }
    }
}
