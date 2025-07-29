package com.example.frontend.auth

import android.app.Activity
import android.util.Log
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.facebook.login.LoginBehavior


object FacebookSignInManager {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val callbackManager: CallbackManager = CallbackManager.Factory.create()

    private var onSuccess: ((String) -> Unit)? = null
    private var onFailure: ((Exception) -> Unit)? = null

    init {
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                Log.d("FacebookSignInManager", "✅ Facebook login succeeded")
                handleFacebookAccessToken(result.accessToken)
            }

            override fun onCancel() {
                onFailure?.invoke(Exception("Facebook login canceled"))
            }

            override fun onError(error: FacebookException) {
                onFailure?.invoke(error)
            }
        })
    }

    fun beginSignIn(
        activity: Activity,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        this.onSuccess = onSuccess
        this.onFailure = onFailure

        LoginManager.getInstance().logOut()

        LoginManager.getInstance().setLoginBehavior(LoginBehavior.DIALOG_ONLY)
        LoginManager.getInstance().logInWithReadPermissions(activity, listOf("email", "public_profile"))
    }



    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)

        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                firebaseAuth.currentUser?.getIdToken(true)
                    ?.addOnSuccessListener { result ->
                        val firebaseToken = result.token
                        if (firebaseToken != null) {
                            onSuccess?.invoke(firebaseToken)
                        } else {
                            onFailure?.invoke(Exception("Firebase token is null"))
                        }
                    }
                    ?.addOnFailureListener { e ->
                        onFailure?.invoke(e)
                    }
            }
            .addOnFailureListener { e ->
                onFailure?.invoke(e)
            }
    }
}
