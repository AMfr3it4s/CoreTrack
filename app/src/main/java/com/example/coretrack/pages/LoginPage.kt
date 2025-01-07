package com.example.coretrack.pages

import SingleWaveShape
import android.content.res.Resources.Theme
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coretrack.AuthState
import com.example.coretrack.AuthViewModel
import com.example.coretrack.R
import com.example.coretrack.ui.theme.Parkinsans


@Composable
fun LoginPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {

    // Email State variable
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.Authenticated -> navController.navigate("home")
            is AuthState.Error -> Toast.makeText(context, (authState.value as AuthState.Error)
                .message, Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ){
            Image(
                painter = painterResource(id = R.drawable.bg),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.TopCenter)
                    .clip(SingleWaveShape)
            )

            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = 142.dp, start = 25.dp, end = 16.dp
                    )
                    ,

                verticalArrangement = Arrangement.Top
            ){
                Text(text = "CoreTrack",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 56.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = Parkinsans
                    )
                )
            }
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 150.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Text(text = "Welcome! Stay active, stay healthy.", fontWeight = FontWeight.Thin, fontFamily = Parkinsans, fontSize = 16.sp, color = MaterialTheme.colorScheme.onPrimary)

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email", fontFamily = Parkinsans, color = MaterialTheme.colorScheme.onPrimary)},
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onPrimary
                )



            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password", fontFamily = Parkinsans, color = MaterialTheme.colorScheme.onPrimary) },
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onPrimary
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    authViewModel.login(email, password)
                },
                enabled = authState.value != AuthState.Loading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(text = "LOGIN", fontFamily = Parkinsans, color = MaterialTheme.colorScheme.primary)
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = {
                    navController.navigate("register")
                }
            ) {
                Text(text = "Don't have an account? Register", fontFamily = Parkinsans, color = MaterialTheme.colorScheme.tertiary)
            }
        }
    }
}
