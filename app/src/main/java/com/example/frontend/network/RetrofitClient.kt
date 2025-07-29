package com.example.frontend.network

import com.example.frontend.api.*
import com.example.frontend.api.UserApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Context
import okhttp3.OkHttpClient


object RetrofitClient {

    private const val BASE_URL = "http://10.0.2.2:8080"

    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(appContext))
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val accessibilityFeatureApi: AccessibilityFeatureApi by lazy {
        retrofit.create(AccessibilityFeatureApi::class.java)
    }

    val destinationApi: DestinationApi by lazy {
        retrofit.create(DestinationApi::class.java)
    }

    val itineraryApi: ItineraryApi by lazy {
        retrofit.create(ItineraryApi::class.java)
    }

    val itineraryStepApi: ItineraryStepApi by lazy {
        retrofit.create(ItineraryStepApi::class.java)
    }

    val savedItineraryApi: SavedItineraryApi by lazy {
        retrofit.create(SavedItineraryApi::class.java)
    }

    val userApi: UserApi by lazy {
        retrofit.create(UserApi::class.java)
    }

    val authApi: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }

    val countryApi: CountryApi by lazy {
        retrofit.create(CountryApi::class.java)
    }

    val userAccessibilityFeatureApi: UserAccessibilityFeatureApi by lazy {
        retrofit.create(UserAccessibilityFeatureApi::class.java)
    }

    val userCountryAccessApi: UserCountryAccessApi by lazy {
        retrofit.create(UserCountryAccessApi::class.java)
    }

    val userSelectedDestinationApi: UserSelectedDestinationApi by lazy {
        retrofit.create(UserSelectedDestinationApi::class.java)
    }

    val reviewApi: ReviewApi by lazy {
        retrofit.create(ReviewApi::class.java)
    }

    val settingApi: SettingApi by lazy {
        retrofit.create(SettingApi::class.java)
    }

    val userSettingApi: UserSettingApi by lazy {
        retrofit.create(UserSettingApi::class.java)
    }
}
