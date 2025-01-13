package com.example.coretrack.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Pedometer(stepCounterViewModel: StepCounterViewModel ) {

    val stepCount = stepCounterViewModel.stepCount.collectAsState()
    val isTracking = remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "Contagem de Passos: ${stepCount.value}")

        Button(
            onClick = {
                if (isTracking.value) {
                    stepCounterViewModel.stopTracking()
                } else {
                    stepCounterViewModel.startTracking(context = context, true)
                }
                isTracking.value = !isTracking.value
            }
        ) {
            Text(text = if (isTracking.value) "Parar Contagem" else "Iniciar Contagem")
        }
    }

    LaunchedEffect(key1 = stepCount.value) {
        //To-DO
    }


}
