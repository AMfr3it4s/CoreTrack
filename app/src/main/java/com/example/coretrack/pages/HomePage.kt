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
        modifier = modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme
                .primary),
    ){
        //Stats GraphSteps per Week
        Box (
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .background(MaterialTheme.colorScheme.primary)

        ){
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center

            ){
                Text(
                    text = "Weakly Activity Report",
                    modifier = Modifier
                        .padding(15.dp)
                    , color = MaterialTheme.colorScheme.surface, fontSize = 15.sp, fontFamily = Parkinsans, fontWeight =
                    FontWeight.Bold)

                Spacer(modifier = Modifier.height(10.dp))

                LineChartScreen()
            }


        }

        Card (
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.primary),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary,

            )

        ){
            Text(text = "Recent Heart Rate Measurement",
                modifier = Modifier
                    .padding(10.dp)
                , fontSize = 14.sp, fontWeight = FontWeight.Normal, fontFamily = Parkinsans, color = MaterialTheme.colorScheme.primary)
            Row (
                modifier = Modifier
                    .padding(25.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = "HeartRate",
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(50.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(text = "Heart Rate", fontFamily = Parkinsans, fontSize = 25.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.primary)

                Spacer(modifier = Modifier.width(16.dp))

                //To be modified to change dynamicaly
                Text(text = "60", fontFamily = Parkinsans, fontSize = 35.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.primary)

                Spacer(modifier = Modifier.width(5.dp))

                Text(text = "bpm", fontFamily = Parkinsans, fontSize = 15.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.primary)
            }

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
                    .background(MaterialTheme.colorScheme.primary),
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

                    Spacer(modifier = Modifier.height(10.dp))

                    Row{
                        Text(text = "2000",
                            modifier = Modifier
                                .padding(5.dp)
                            , fontSize = 20.sp, fontWeight = FontWeight.Bold, fontFamily = Parkinsans, color = MaterialTheme.colorScheme.primary)


                        Text(text = "Kcal",
                            modifier = Modifier
                                .padding(7.dp)
                            , fontSize = 10.sp, fontWeight = FontWeight.Normal, fontFamily = Parkinsans, color = MaterialTheme.colorScheme.primary)
                    }

                }
            }

            // Card 2
            Card(
                modifier = Modifier
                    .height(140.dp)
                    .width(135.dp)
                    .padding(8.dp)
                    .background(MaterialTheme.colorScheme.primary),
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

                    Spacer(modifier = Modifier.height(10.dp))

                    Row{
                        Text(text = "10",
                            modifier = Modifier
                                .padding(5.dp)
                            , fontSize = 20.sp, fontWeight = FontWeight.Bold, fontFamily = Parkinsans, color = MaterialTheme.colorScheme.primary)


                        Text(text = "Km",
                            modifier = Modifier
                                .padding(7.dp)
                            , fontSize = 10.sp, fontWeight = FontWeight.Normal, fontFamily = Parkinsans, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            // Card 3
            Card(
                modifier = Modifier
                    .height(140.dp)
                    .width(135.dp)
                    .padding(8.dp)
                    .background(MaterialTheme.colorScheme.primary),
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

                    Spacer(modifier = Modifier.height(10.dp))

                    Row{
                        Text(text = "60",
                            modifier = Modifier
                                .padding(5.dp)
                            , fontSize = 20.sp, fontWeight = FontWeight.Bold, fontFamily = Parkinsans, color = MaterialTheme.colorScheme.primary)


                        Text(text = "Min",
                            modifier = Modifier
                                .padding(7.dp)
                            , fontSize = 10.sp, fontWeight = FontWeight.Normal, fontFamily = Parkinsans, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }


    }
}