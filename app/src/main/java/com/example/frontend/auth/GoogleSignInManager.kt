package com.example.frontend.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class GoogleSignInManager(private val context: Context) {

    private val oneTapClient: SignInClient = Identity.getSignInClient(context)
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val signInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(CLIENT_ID)
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        .setAutoSelectEnabled(false)
        .build()

    fun beginSignIn(
        activity: Activity,
        requestCode: Int,
        onFailure: (Exception) -> Unit
    ) {
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(activity) { result: BeginSignInResult ->
                activity.startIntentSenderForResult(
                    result.pendingIntent.intentSender,
                    requestCode,
                    null,
                    0,
                    0,
                    0
                )
            }
            .addOnFailureListener(activity) { e ->
                Log.e("GoogleSignInManager", "Sign-in failed", e)
                onFailure(e)
            }
    }

    fun handleSignInResult(
        data: Intent?,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        try {
            val credential: SignInCredential = oneTapClient.getSignInCredentialFromIntent(data)
            val googleIdToken = credential.googleIdToken

            if (googleIdToken != null) {
                val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)

                firebaseAuth.signInWithCredential(firebaseCredential)
                    .addOnSuccessListener { authResult ->
                        firebaseAuth.currentUser?.getIdToken(true)
                            ?.addOnSuccessListener { result ->
                                val firebaseToken = result.token
                                if (firebaseToken != null) {
                                    onSuccess(firebaseToken)
                                } else {
                                    onFailure(Exception("Firebase token is null"))
                                }
                            }
                            ?.addOnFailureListener { e ->
                                onFailure(e)
                            }
                    }
                    .addOnFailureListener { e ->
                        onFailure(e)
                    }
            } else {
                throw IllegalArgumentException("Google ID token is null")
            }
        } catch (e: ApiException) {
            onFailure(e)
        }
    }

    companion object {
        const val CLIENT_ID = "511512637607-f89n4krl8vnqf7q673k8jm4kgshtk8ip.apps.googleusercontent.com"
    }
}
