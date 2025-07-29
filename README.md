# Inclusive Trip Planner – Frontend

![License](https://img.shields.io/github/license/DavidCC812/inclusive-trip-planner-frontend?style=flat-square)
![Build Status](https://github.com/DavidCC812/inclusive-trip-planner-frontend/actions/workflows/ci.yml/badge.svg)
![Kotlin](https://img.shields.io/badge/Kotlin-1.9%2B-blueviolet.svg)
![Jetpack Compose](https://img.shields.io/badge/Jetpack--Compose-UI-green.svg)
![Android](https://img.shields.io/badge/Android-API%2030%2B-yellow.svg)

Backend repository: [inclusive-trip-planner-backend](https://github.com/DavidCC812/inclusive-trip-planner-backend)

## Overview

This repository contains the frontend application for the Inclusive Trip Planner — a mobile-first travel assistant designed to help users with disabilities discover and plan accessible itineraries. This project was developed as part of a Master 2 admission portfolio and builds on prior thesis work originally named “moonshot-project.”

The mobile app is built using Jetpack Compose (Kotlin), follows MVVM architecture, and integrates with a secure backend powered by Spring Boot.

## Table of Contents

- [Key Features](#key-features)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Setup Instructions](#setup-instructions)
- [Usage](#usage)
- [Testing](#testing)
- [Project History](#project-history)
- [Contact](#contact)

## Key Features

- Accessible onboarding with destination, accessibility features, and country selection
- Predefined itineraries with real accessibility filters (wheelchair access, vision/hearing friendly)
- Save itineraries locally and to a secure backend
- User reviews for each itinerary
- Google One-Tap sign-in using Firebase authentication
- Live integration with deployed backend via Retrofit and REST APIs
- Unit-tested ViewModels and CI for safe commits

## Architecture

The app follows a clean MVVM (Model–View–ViewModel) structure, organized as follows:

```
api/                 Retrofit API interfaces  
auth/                Authentication logic (Firebase)  
components/          Reusable Jetpack Compose UI components  
models/              Kotlin data models (DTOs, responses, requests)  
network/             Network configuration (e.g. Retrofit setup)  
screens/             Top-level Composable screens  
storage/             Local storage and shared preferences  
viewmodels/          ViewModel classes and UI logic  
test/.../viewmodels/ Unit tests for all ViewModels  
```

This structure separates UI rendering (screens/components) from business logic (viewmodels) and data sources (api/network/models), ensuring modularity, testability, and scalability.

## Tech Stack

- Language: Kotlin
- UI: Jetpack Compose
- Architecture: MVVM
- API: Retrofit + Gson
- Auth: Firebase (Google Sign-In, Facebook Sign-in)
- Testing: JUnit, Coroutine testing, Mock APIs
- CI: GitHub Actions

## Setup Instructions

1. **Clone the Repository**

   ```bash
   git clone https://github.com/DavidCC812/inclusive-trip-planner-frontend.git
   cd inclusive-trip-planner-frontend
   ```

2. **Open in Android Studio**

   - Open the project using **Android Studio Hedgehog** or later  
   - Allow **Gradle** to sync and resolve all dependencies

3. **Firebase Configuration**

   - Replace the existing `google-services.json` in the project root with your own Firebase config  
   - Enable **Google Sign-In** in your Firebase console  
   - No need to use the **Assistant tab** — Firebase Authentication is already integrated

## Backend Integration

This app communicates with a secured REST API hosted at:

[https://inclusive-trip-backend.onrender.com](https://inclusive-trip-backend.onrender.com)

All data is exchanged via Retrofit, including user onboarding, itinerary saving, reviews, and preferences. The app authenticates users with Firebase and exchanges the resulting ID token for a JWT via `/api/auth/firebase`.

## Usage

Once the app is installed:

1. Sign up using Google One-Tap
2. Select your accessibility needs and target countries
3. Browse and save accessible itineraries
4. Leave reviews, track favorites, and plan your trip

## Testing

To run unit tests:

   ```bash
   ./gradlew test
   ```

This includes coverage for:

- Onboarding flows
- ViewModel state handling
- API error conditions
- Itinerary and settings logic

GitHub Actions runs all tests on each commit to develop and main.

## Project History

- Initial Thesis Phase: March – June 2025 (moonshot-project)
- Public Repository Creation & Refactor: July 2025
- Feature Freeze & CI Setup: July 27, 2025

## Contact

**David Cuahonte Cuevas**  
[GitHub](https://github.com/DavidCC812)  
[LinkedIn](https://www.linkedin.com/in/david-cuahonte-527781221/)
