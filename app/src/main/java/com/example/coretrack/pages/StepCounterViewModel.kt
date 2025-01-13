package com.example.coretrack.pages

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StepCounterViewModel : ViewModel() {

    private val _stepCount = MutableStateFlow(0)
    val stepCount: StateFlow<Int> get() = _stepCount

    private var lastLocation: Location? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationCallback: LocationCallback? = null

    // FusedLocationClient
    fun initFusedLocationClient(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }

    @SuppressLint("MissingPermission")
    fun startTracking(context: Context, permissionGranted: Boolean) {

        if (!::fusedLocationClient.isInitialized) {
            return
        }

        if (permissionGranted) {
            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setMinUpdateDistanceMeters(0f)
                .setIntervalMillis(10000)
                .build()

            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    locationResult.lastLocation?.let { location ->
                        updateStepCount(location)
                    }
                }
            }

            // Location Update
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback!!,
                Looper.getMainLooper()
            )
        } else {
            Toast.makeText(context, "Location permission needed", Toast.LENGTH_LONG).show()
        }
    }

    fun stopTracking() {
        locationCallback?.let { callback ->
            fusedLocationClient.removeLocationUpdates(callback)
            locationCallback = null
        }
    }

    fun updateStepCount(location: Location) {
        val distance = lastLocation?.distanceTo(location) ?: 0f
        lastLocation = location

        val steps = calculateSteps(distance)
        _stepCount.value += steps
    }

    fun calculateSteps(distance: Float): Int {
        val stepLength = 0.7f
        return (distance / stepLength).toInt()
    }
}


