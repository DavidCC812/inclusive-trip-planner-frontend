package com.example.frontend.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*

@Composable
fun MapScreen(stepLocations: List<LatLng>, selectedStep: Int) {
    val paris = LatLng(48.8566, 2.3522)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            stepLocations.getOrNull(selectedStep) ?: paris, 14f
        )
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        stepLocations.forEachIndexed { index, location ->
            Marker(
                state = rememberMarkerState(position = location),
                title = "Step ${index + 1}",
                snippet = "Itinerary Step",
                icon = BitmapDescriptorFactory.defaultMarker(
                    if (index == selectedStep) BitmapDescriptorFactory.HUE_BLUE
                    else BitmapDescriptorFactory.HUE_RED
                )
            )
        }
    }
}