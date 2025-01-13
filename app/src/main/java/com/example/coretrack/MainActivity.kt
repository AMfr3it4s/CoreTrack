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
import com.example.coretrack.utils.NetworkUtils
import com.example.coretrack.workers.scheduleSyncWorker
import com.google.firebase.auth.FirebaseAuth


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val context = this
        val networkUtils = NetworkUtils(context)
        val authViewModel : AuthViewModel by viewModels()
        val userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
        networkUtils.isConnected().observe(this) { isConnected ->
            if (isConnected) {
                scheduleSyncWorker(context, userId) // Sync data when online
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
}

