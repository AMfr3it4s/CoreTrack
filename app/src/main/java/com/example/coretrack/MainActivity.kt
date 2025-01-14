package com.example.coretrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.coretrack.ui.theme.CoreTrackTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val authViewModel: AuthViewModel by viewModels()


        stepCounterViewModel.initFusedLocationClient(this)


        if (hasLocationPermission()) {
            stepCounterViewModel.startTracking(this, true)
        } else {
            requestLocationPermission()
        }

        // Composable UI
        val context = this
        val userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
        val networkUtils = NetworkUtils(context)
        networkUtils.isConnected().observe(this) { isConnected ->
            if (isConnected) {
                scheduleSyncWorker(context, userId) // Sync data when online
            }
        }
        setContent {
            CoreTrackTheme(){
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Navigation(modifier = Modifier.padding(innerPadding), authViewModel = authViewModel)
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
                    Navigation(modifier = Modifier.padding(innerPadding) ,authViewModel = authViewModel, userId = userId)
                }
            }
        }
    }
}

