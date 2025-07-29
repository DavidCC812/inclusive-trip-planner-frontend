package com.example.frontend.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import com.example.frontend.viewmodels.ProfileViewModel
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.frontend.components.CustomChipGrid
import com.example.frontend.viewmodels.UserViewModel
import com.example.frontend.viewmodels.AccessibilityFeatureViewModel
import androidx.compose.foundation.Image
import com.example.frontend.R
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import java.util.UUID


@Composable
fun ProfileScreen(navController: NavHostController) {
    val userViewModel: UserViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()
    val accessibilityFeatureViewModel: AccessibilityFeatureViewModel = viewModel()

    val user by userViewModel.user.collectAsState()
    val selectedLabels by accessibilityFeatureViewModel.selectedLabels.collectAsState()
    val isFeatureLoading by accessibilityFeatureViewModel.isLoading.collectAsState()
    val userId = user?.id

    // Load user on start
    LaunchedEffect(Unit) {
        userViewModel.loadUserFromToken()
    }

    // Fetch accessibility features
    LaunchedEffect(Unit) {
        accessibilityFeatureViewModel.fetchAccessibilityFeatures()
    }

    // Fetch user-specific features once data is ready
    LaunchedEffect(userId, isFeatureLoading) {
        if (userId != null && !isFeatureLoading) {
            accessibilityFeatureViewModel.fetchUserFeatures(userId)
        }
    }

    // Sync with profile view model
    LaunchedEffect(userId, selectedLabels) {
        user?.let {
            profileViewModel.updateProfile(
                newUserId = UUID.fromString(it.id),
                newName = it.name,
                newEmail = it.email,
                newPhone = it.phone,
                newAccessibility = selectedLabels.joinToString(", ")
            )
        }
    }

    Scaffold(
        topBar = { ProfileTopBar(navController) },
        bottomBar = { BottomNavBar(navController, selectedScreen = "profile") }
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileHeader(profileViewModel.name.value)

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 6.dp,
                    shape = RoundedCornerShape(12.dp),
                    backgroundColor = Color.White
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        AccountDetails(
                            email = profileViewModel.email.value,
                            phone = profileViewModel.phone.value,
                            accessibilityChips = selectedLabels
                        )
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        ActivitySection(navController)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun ProfileTopBar(navController: NavHostController) {
    TopAppBar(
        backgroundColor = Color(0xFF9AA6B2),
        elevation = 4.dp,
        modifier = Modifier.height(56.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Centered Logo
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "App Logo",
                colorFilter = ColorFilter.tint(Color.Black),
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.Center)
            )

            // Right-aligned Icons
            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.navigate("notifications") }) {
                    Icon(
                        Icons.Filled.Notifications,
                        contentDescription = "Notifications",
                        tint = Color.Black,
                        modifier = Modifier.size(28.dp)
                    )
                }
                IconButton(onClick = { navController.navigate("settings") }) {
                    Icon(
                        Icons.Filled.Settings,
                        contentDescription = "Settings",
                        tint = Color.Black,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileHeader(name: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .background(Color.Gray)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = name, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Text(text = "Traveler & Explorer", fontSize = 14.sp, color = Color.LightGray)
    }
}

@Composable
fun AccountDetails(
    email: String,
    phone: String,
    accessibilityChips: List<String>
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Account Information", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))

        ProfileInfoItem(label = "Email", value = email, showDivider = true)
        ProfileInfoItem(label = "Phone", value = phone, showDivider = true)

        Spacer(modifier = Modifier.height(8.dp))

        Text("Accessibility Preferences", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)

        if (accessibilityChips.isEmpty()) {
            Text("None", fontSize = 16.sp, color = Color.Black)
        } else {
            CustomChipGrid(
                options = accessibilityChips,
                selectedOptions = accessibilityChips.toSet(),
                onSelectionChanged = {}, // read-only
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp), thickness = 1.dp, color = Color.LightGray)
    }
}


@Composable
fun ProfileInfoItem(label: String, value: String, showDivider: Boolean) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
        Text(text = value, fontSize = 16.sp, color = Color.Black)
        if (showDivider) {
            Divider(modifier = Modifier.padding(vertical = 4.dp), thickness = 1.dp, color = Color.LightGray)
        }
    }
}

@Composable
fun ActivitySection(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("My Activity", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))
        ActivityItem("Reviews", onClick = { navController.navigate("my_reviews") })
    }
}

@Composable
fun ActivityItem(title: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF9AA6B2), contentColor = Color.Black)
    ) {
        Text(title, fontSize = 16.sp)
    }
}
