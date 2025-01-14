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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.coretrack.ui.theme.Parkinsans
import java.time.format.TextStyle

@Composable
fun HomePage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel)
{
    val authState = authViewModel.authState.observeAsState()
    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.Unauthenticated -> navController.navigate("login")
            else -> Unit
        }
    }

    Column (
        modifier = modifier.fillMaxSize(),
    ){
        //Stats GraphSteps per Week
        Box (
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .background(Color.Blue)
        ){
            Text(
                text = "Weakly Activity Report",
                modifier = Modifier
                    .padding(15.dp)
                    .align(
                        Alignment.TopStart
                    )
                , color = MaterialTheme.colorScheme.primary, fontSize = 15.sp, fontFamily = Parkinsans, fontWeight =
            FontWeight.Bold)
        }

        Card (
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp)
                .background(Color(0xFFE3F2FD)),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )

        ){
            Text(text = "Recent Heart Rate Measurement",
                modifier = Modifier
                    .padding(10.dp)
                , fontSize = 14.sp, fontWeight = FontWeight.Normal, fontFamily = Parkinsans, color = MaterialTheme.colorScheme.primary)

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Card 1
            Card(
                modifier = Modifier
                    .height(140.dp)
                    .width(135.dp)
                    .padding(8.dp)
                    .background(Color(0xFFE3F2FD)),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )


            ) {
                Column(
                    modifier = Modifier.padding(5.dp)
                ) {
                    Text(text = "Calories",
                        modifier = Modifier
                            .padding(5.dp)
                        , fontSize = 14.sp, fontWeight = FontWeight.Normal, fontFamily = Parkinsans, color = MaterialTheme.colorScheme.primary)
                }
            }

            // Card 2
            Card(
                modifier = Modifier
                    .height(140.dp)
                    .width(135.dp)
                    .padding(8.dp)
                    .background(Color(0xFFE3F2FD)),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )

            ) {
                Column(
                    modifier = Modifier.padding(5.dp)
                ) {
                    Text(text = "Distance",
                        modifier = Modifier
                            .padding(5.dp)
                        , fontSize = 14.sp, fontWeight = FontWeight.Normal, fontFamily = Parkinsans, color = MaterialTheme.colorScheme.primary)
                }
            }

            // Card 3
            Card(
                modifier = Modifier
                    .height(140.dp)
                    .width(135.dp)
                    .padding(8.dp)
                    .background(Color(0xFFE3F2FD)),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )

                ) {
                Column(
                    modifier = Modifier.padding(5.dp)
                ) {
                    Text(text = "Time",
                        modifier = Modifier
                            .padding(5.dp)
                        , fontSize = 14.sp, fontWeight = FontWeight.Normal, fontFamily = Parkinsans , color = MaterialTheme.colorScheme.primary)
                }
            }
        }


    }
}