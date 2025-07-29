plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.gms.google-services")
    id("jacoco")
}

// Custom versioning logic based on -Psnapshot
val isSnapshot = project.findProperty("snapshot") == "true"
val baseVersionName = "1.0.0"
val resolvedVersionName = if (isSnapshot) "$baseVersionName-SNAPSHOT" else baseVersionName

android {
    namespace = "com.example.frontend"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.frontend"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = resolvedVersionName
    }

    lint {
        disable += "UnrememberedGetBackStackEntry"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    // Core libraries
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.ktx.v1150)
    implementation(libs.androidx.lifecycle.runtime.ktx.v287)

    // Retrofit
    implementation(libs.retrofit)

    // Jetpack Compose UI libraries
    implementation(libs.ui)
    implementation(libs.androidx.material)
    implementation(libs.ui.tooling.preview)
    implementation(libs.androidx.activity.compose.v172)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.navigation.compose.v275)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.accompanist.flowlayout)
    implementation(libs.coil.compose)

    // Testing libraries
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test.v173)
    testImplementation(libs.turbine)

    // Google libraries
    implementation(libs.play.services.maps)
    implementation(libs.android.maps.utils)
    implementation(libs.maps.compose)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)
    implementation(libs.play.services.auth)
    implementation(libs.facebook.login)

    // Converter (for JSON → Kotlin object)
    implementation(libs.converter.gson)
    implementation(libs.gson)

    implementation(libs.androidx.datastore.preferences)

    // Unit testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

jacoco {
    toolVersion = "0.8.11"
}

tasks.create<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    classDirectories.setFrom(
        layout.buildDirectory.dir("tmp/kotlin-classes/debug").map {
            fileTree(it) {
                exclude(
                    "**/R.class",
                    "**/R$*.class",
                    "**/BuildConfig.*",
                    "**/di/**",
                    "**/Hilt*.*",
                    "**/Dagger*.*"
                )
            }
        }
    )

    sourceDirectories.setFrom(files("src/main/java", "src/main/kotlin"))
    executionData.setFrom(files(layout.buildDirectory.file("jacoco/testDebugUnitTest.exec")))
}
