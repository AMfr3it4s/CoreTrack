package com.example.coretrack

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.coretrack.pages.BottomNavigationBar
import com.example.coretrack.pages.HeartPage
import com.example.coretrack.pages.HomePage
import com.example.coretrack.pages.LoginPage
import com.example.coretrack.pages.Pedometer
import com.example.coretrack.pages.RegisterPage
import com.example.coretrack.pages.StepCounterViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    userId: String
) {
    val navController = rememberNavController()
    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (currentRoute(navController) in listOf("home", "heart", "activities")) {
                BottomNavigationBar(navController)
            }
        }
    ) { innerPadding ->
        val currentRoute = currentRoute(navController)
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = if (currentRoute in listOf("home", "heart", "activities")) {
                Modifier.padding(innerPadding)
            } else {
                Modifier
            }
        ) {
            composable("login") {
                LoginPage(
                    navController = navController,
                    authViewModel = authViewModel
                )
            }
            composable("register") {
                RegisterPage(
                    navController = navController,
                    authViewModel = authViewModel,
                )
            }
            composable("home") {
                HomePage(
                    navController = navController,
                    authViewModel = authViewModel
                )
            }
            composable("heart") {
                HeartPage(
                    navController = navController,
                    userId = userId
                )
            }
            composable("activities") {
                    backStackEntry ->
                val isDarkMode by remember { mutableStateOf(false) }
                val onToggleTheme: (Boolean) -> Unit = { newMode ->
                    // Lógica para alternar entre os modos claro e escuro
                }
                val stepCounterViewModel: StepCounterViewModel = viewModel()
                Pedometer( authViewModel = authViewModel, navController = navController, stepCounterViewModel = stepCounterViewModel)
            }
        }
    }
}


@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}