package com.example.frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.frontend.screens.*
import com.example.frontend.viewmodels.SavedItinerariesViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontend.viewmodels.MyReviewsViewModel
import com.example.frontend.viewmodels.ProfileViewModel
import com.example.frontend.storage.TokenManager
import androidx.navigation.compose.navigation
import com.example.frontend.network.RetrofitClient
import android.content.Intent
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.frontend.auth.GoogleSignInManager
import com.example.frontend.auth.FacebookSignInManager
import com.example.frontend.models.FirebaseSignInRequest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import androidx.navigation.NavHostController
import com.example.frontend.viewmodels.ItineraryViewModel


class MainActivity : ComponentActivity() {

    private lateinit var googleSignInManager: GoogleSignInManager
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RetrofitClient.init(applicationContext)
        googleSignInManager = GoogleSignInManager(this)

        setContent {
            navController = rememberNavController()
            AppNavigator(navController)
        }
    }

    fun onFacebookTokenReceived(idToken: String, onNavigateHome: () -> Unit) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.authApi.loginWithFacebook(FirebaseSignInRequest(idToken))
                if (response.isSuccessful) {
                    val authResponse = response.body()
                    Log.d("MainActivity", "Backend login (Facebook) successful: $authResponse")

                    if (authResponse != null) {
                        TokenManager.saveToken(applicationContext, authResponse.token)
                        Log.d("MainActivity", "JWT saved. Welcome ${authResponse.name}")

                        Log.d("MainActivity", "Calling onNavigateHome()")
                        onNavigateHome()
                    }

                } else {
                    Log.e("MainActivity", "Backend login failed: ${response.code()} ${response.message()}")
                }
            } catch (e: HttpException) {
                Log.e("MainActivity", "HTTP error", e)
            } catch (e: Exception) {
                Log.e("MainActivity", "Unexpected error", e)
            }
        }
    }


    @Deprecated("Use registerForActivityResult instead", level = DeprecationLevel.WARNING)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (FacebookSignInManager.callbackManager.onActivityResult(requestCode, resultCode, data)) {
            Log.d("MainActivity", "Facebook callback handled successfully")
            return
        }

        super.onActivityResult(requestCode, resultCode, data)

        Log.d("MainActivity", "onActivityResult called with requestCode=$requestCode")

        if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE) {
            googleSignInManager.handleSignInResult(
                data,
                onSuccess = { idToken ->
                    Log.d("MainActivity", "Firebase Google Sign-In successful. ID Token: $idToken")

                    lifecycleScope.launch {
                        try {
                            val response = RetrofitClient.authApi.loginWithFirebase(FirebaseSignInRequest(idToken))
                            if (response.isSuccessful) {
                                val authResponse = response.body()
                                Log.d("MainActivity", "Backend login successful: $authResponse")

                                if (authResponse != null) {
                                    TokenManager.saveToken(applicationContext, authResponse.token)
                                    Log.d("MainActivity", "JWT saved. Welcome ${authResponse.name}")

                                    navController.navigate("home") {
                                        popUpTo("welcome") { inclusive = true }
                                    }
                                }
                            } else {
                                Log.e("MainActivity", "Backend login failed: ${response.code()} ${response.message()}")
                            }
                        } catch (e: HttpException) {
                            Log.e("MainActivity", "HTTP error", e)
                        } catch (e: Exception) {
                            Log.e("MainActivity", "Unexpected error", e)
                        }
                    }
                },
                onFailure = { e ->
                    Log.e("MainActivity", "Firebase Google Sign-In failed", e)
                }
            )
        }
    }

    companion object {
        private const val GOOGLE_SIGN_IN_REQUEST_CODE = 1001
    }
}

@Composable
fun AppNavigator(navController: NavHostController) {
    val profileViewModel: ProfileViewModel = viewModel()
    val savedItinerariesViewModel = remember { SavedItinerariesViewModel() }
    val myReviewsViewModel = remember { MyReviewsViewModel() }
    val itineraryViewModel: ItineraryViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "welcome"
    ) {
        composable("welcome") {
            WelcomeScreen(navController)
        }
        composable("email") {
            EmailScreen(navController)
        }
        composable("phone") {
            PhoneScreen(navController)
        }
        navigation(
            startDestination = "signup_name",
            route = "signup_flow"
        ) {
            composable("signup_name") { SignUpNameScreen(navController) }
            composable("signup_email") { SignUpEmailScreen(navController) }
            composable("signup_phone") { SignUpPhoneScreen(navController) }
            composable("signup_password") { SignUpPasswordScreen(navController) }
            composable("signup_accessibility") { SignUpAccessibilityScreen(navController) }
            composable("signup_countries") { SignUpCountriesScreen(navController) }

            composable(
                "otp_verification/{verificationType}/{identifier}",
                arguments = listOf(
                    navArgument("verificationType") { type = NavType.StringType },
                    navArgument("identifier") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val verificationType =
                    backStackEntry.arguments?.getString("verificationType") ?: "email"
                val identifier = backStackEntry.arguments?.getString("identifier") ?: ""

                OTPVerificationScreen(navController, verificationType, identifier)
            }
        }
        composable("forgot_email") {
            ForgotAccountScreen(navController)
        }
        composable("forgot_password") {
            ForgotPasswordScreen(navController)
        }
        composable("change_password") {
            ChangePasswordScreen(navController)
        }
        composable(
            "login_with_password/{identifier}",
            arguments = listOf(navArgument("identifier") { type = NavType.StringType })
        ) { backStackEntry ->
            val identifier = backStackEntry.arguments?.getString("identifier") ?: ""
            LoginWithPasswordScreen(navController, identifier)
        }
        composable("home") {
            HomeScreen(navController, savedItinerariesViewModel, itineraryViewModel)
        }
        composable(
            "itinerary_details/{itineraryId}",
            arguments = listOf(navArgument("itineraryId") { type = NavType.StringType })
        ) { backStackEntry ->
            val itineraryId = backStackEntry.arguments?.getString("itineraryId") ?: "00000000-0000-0000-0000-000000000000"
            ItineraryDetailsScreen(navController, itineraryId, savedItinerariesViewModel, itineraryViewModel)
        }

        composable(
            "itinerary_steps/{itineraryId}",
            arguments = listOf(navArgument("itineraryId") { type = NavType.StringType })
        ) { backStackEntry ->
            val itineraryIdStr = backStackEntry.arguments?.getString("itineraryId") ?: "00000000-0000-0000-0000-000000000000"
            ItineraryStepsScreen(navController, itineraryIdStr)
        }
        composable("write_review/{itineraryId}") { backStackEntry ->
            val itineraryId = backStackEntry.arguments?.getString("itineraryId") ?: "00000000-0000-0000-0000-000000000000"
            val userId = profileViewModel.userId.value
            WriteAReviewScreen(navController, reviewViewModel = viewModel(), userId = userId, itineraryId = itineraryId)
        }

        composable("my_reviews") {
            MyReviewsScreen(navController, myReviewsViewModel)
        }
        composable("notifications") {
            NotificationsScreen(navController)
        }
        composable("profile") {
            ProfileScreen(navController)
        }
        composable("settings") {
            SettingsScreen(navController, profileViewModel)
        }
        composable("search") {
            SearchScreen(navController, itineraryViewModel)
        }
        composable("saved_itineraries") {
            SavedItinerariesScreen(navController, savedItinerariesViewModel)
        }
        composable("privacy_policy") {
            PrivacyPolicyScreen(navController)
        }
    }
}