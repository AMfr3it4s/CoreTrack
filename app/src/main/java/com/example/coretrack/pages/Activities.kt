package com.example.coretrack.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coretrack.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivitiesPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    isDarkMode: Boolean,
    onToggleTheme: (Boolean) -> Unit
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    var showLogoutDialog by remember { mutableStateOf(false) }



    // Logout Confirmation Dialog
    LogoutConfirmationDialog(showLogoutDialog, authViewModel, navController) { showLogoutDialog = it }
}

@Composable
fun DrawerContent(
    isDarkMode: Boolean,
    onToggleTheme: (Boolean) -> Unit,
    showLogoutDialog: Boolean,
    onShowLogoutDialog: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "Menu", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        // Switch Toggle para o Modo Escuro
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Dark Mode", fontSize = 18.sp)
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = isDarkMode,
                onCheckedChange = onToggleTheme
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botão de Logout
        Button(
            onClick = { onShowLogoutDialog(true) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Logout")
        }
    }
}

@Composable
fun ScaffoldContent(
    innerPadding: PaddingValues,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding), // Aplica o innerPadding do Scaffold
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Activities Page", fontSize = 32.sp)
    }
}

@Composable
fun LogoutConfirmationDialog(
    showLogoutDialog: Boolean,
    authViewModel: AuthViewModel,
    navController: NavController,
    onShowLogoutDialog: (Boolean) -> Unit
) {
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { onShowLogoutDialog(false) },
            title = { Text(text = "Confirm Logout") },
            text = { Text(text = "Are you sure you want to logout?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onShowLogoutDialog(false)
                        authViewModel.signOut()
                        navController.navigate("login")
                    }
                ) {
                    Text(text = "Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { onShowLogoutDialog(false) }) {
                    Text(text = "No")
                }
            }
        )
    }
}

