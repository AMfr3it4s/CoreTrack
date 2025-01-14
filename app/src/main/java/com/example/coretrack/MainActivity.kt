package com.example.coretrack

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.example.coretrack.pages.StepCounterViewModel
import com.example.coretrack.ui.theme.CoreTrackTheme
import com.example.coretrack.utils.NetworkUtils
import com.example.coretrack.workers.scheduleSyncWorker
import com.google.firebase.auth.FirebaseAuth


class MainActivity : ComponentActivity() {
    private val stepCounterViewModel: StepCounterViewModel by viewModels()
    private val REQUEST_LOCATION_PERMISSION = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val context = this
        val authViewModel : AuthViewModel by viewModels()
        val userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
        val networkUtils = NetworkUtils(context)
        stepCounterViewModel.initFusedLocationClient(this)
        networkUtils.isConnected().observe(this) { isConnected ->
            if (isConnected) {
                scheduleSyncWorker(context, userId) // Sync data when online
            }
        }
        if (hasLocationPermission()) {
            stepCounterViewModel.startTracking(this, true)
        } else {
            requestLocationPermission()
        }

        // Composable UI
        setContent {
            CoreTrackTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Navigation(modifier = Modifier.padding(innerPadding), authViewModel = authViewModel)
                }
            }
        }
        setContent {
            CoreTrackTheme(){
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Navigation(modifier = Modifier.padding(innerPadding) ,authViewModel = authViewModel, userId = userId)
                }
            }
        }
    }
    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }


    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_LOCATION_PERMISSION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                stepCounterViewModel.startTracking(this, true)
            } else {
                Toast.makeText(this, "Location Permission Denied", Toast.LENGTH_SHORT).show()
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Toast.makeText(this, "Permission was permanently denied. Please enable it in settings.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
}

