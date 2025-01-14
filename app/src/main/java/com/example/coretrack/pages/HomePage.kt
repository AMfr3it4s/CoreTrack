package com.example.coretrack.pages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coretrack.AuthState
import com.example.coretrack.AuthViewModel
import com.example.coretrack.model.HistoryRecord
import com.example.coretrack.ui.theme.Parkinsans
import java.time.format.TextStyle

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {

    val authState = authViewModel.authState.observeAsState()
    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("login")
            else -> Unit
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
    ) {
        // Graph Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Weekly Activity Report",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 15.sp,
                    fontFamily = Parkinsans,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                LineChartScreen()
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Card BPMs
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(175.dp)
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(
                    text = "Recent Heart Rate Measurement",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = Parkinsans,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.Favorite,
                        contentDescription = "HeartRate",
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(50.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = "Heart Rate",
                        fontFamily = Parkinsans,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = "60",
                        fontFamily = Parkinsans,
                        fontSize = 35.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Text(
                        text = "bpm",
                        fontFamily = Parkinsans,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Line of Cards for Calories Distance Time
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Card 1 - Calories
            CardStats(title = "Calories", value = "500", unit = "Kcal")

            // Card 2 - Distance
            CardStats(title = "Distance", value = "2", unit = "Km")

            // Card 3 - Time
            CardStats(title = "Time", value = "60", unit = "Min")
        }
    }
}

// Reusable Composable for Cards
@Composable
fun CardStats(title: String, value: String, unit: String) {
    Card(
        modifier = Modifier
            .height(140.dp)
            .width(135.dp)
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = Parkinsans,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row {
                Text(
                    text = value,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Parkinsans,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = unit,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = Parkinsans,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
