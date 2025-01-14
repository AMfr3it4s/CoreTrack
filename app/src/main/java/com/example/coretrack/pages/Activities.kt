package com.example.coretrack.pages

import android.annotation.SuppressLint
import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.coretrack.AuthState
import com.example.coretrack.AuthViewModel
import com.example.coretrack.ui.theme.Parkinsans
import com.google.android.gms.location.LocationServices

import com.google.android.gms.tasks.Task

import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Pedometer(stepCounterViewModel: StepCounterViewModel, authViewModel: AuthViewModel, navController: NavController) {
    val stepCount = stepCounterViewModel.stepCount.collectAsState()
    val isTracking = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val authState = authViewModel.authState.observeAsState()

    // Initialize FusedLocationProviderClient for fetching the current location
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val currentLocation = remember { mutableStateOf<Location?>(null) }

    // Request current location
    LaunchedEffect(Unit) {
        val locationTask: Task<Location> = fusedLocationClient.lastLocation
        locationTask.addOnSuccessListener { location: Location? ->
            currentLocation.value = location
        }
    }


    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("login")
            else -> Unit
        }
    }

    // Drawer & top bar
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        scrimColor = MaterialTheme.colorScheme.secondary.copy(0.8f),
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.primary
            ) {
                DrawerContent(authViewModel = authViewModel)
            }
        }
    ) {
        Scaffold(
            topBar = {
                Topbar(scrollBehavior = scrollBehavior, onOpenDrawer = {
                    scope.launch {
                        drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }
                })
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(30.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // Tracking button
                    Button(
                        onClick = {
                            if (isTracking.value) {
                                stepCounterViewModel.stopTracking()
                            } else {
                                stepCounterViewModel.startTracking(context = context, true)
                            }
                            isTracking.value = !isTracking.value
                        },
                        modifier = Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(100.dp))
                            .background(MaterialTheme.colorScheme.tertiary),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isTracking.value) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        Text(
                            text = if (isTracking.value) "Parar" else "Iniciar",
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = Parkinsans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 35.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    if (isTracking.value) {
                        Text(
                            text = "Current Step Count ${stepCount.value}",
                            color = MaterialTheme.colorScheme.surface,
                            fontFamily = Parkinsans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Topbar(modifier: Modifier = Modifier, scrollBehavior: TopAppBarScrollBehavior, onOpenDrawer: () -> Unit) {
    CenterAlignedTopAppBar(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(100.dp)),
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondary,
        ),
        windowInsets = WindowInsets(top = 0.dp),
        title = {
            Text(
                "Activity Tracker",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 17.sp,
                fontFamily = Parkinsans,
            )
        },
        navigationIcon = {
            Icon(
                imageVector = Icons.Rounded.Menu,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(start = 16.dp, end = 8.dp)
                    .size(27.dp)
                    .clickable {
                        onOpenDrawer()
                    }
            )
        }
    )
}

@Composable
fun DrawerContent(modifier: Modifier = Modifier, authViewModel: AuthViewModel) {
    Text(
        text = "CoreTrack Settings",
        fontSize = 24.sp,
        modifier = Modifier.padding(16.dp),
        color = MaterialTheme.colorScheme.surface,
        fontFamily = Parkinsans
    )
    NavigationDrawerItem(
        icon = {
            Icon(
                imageVector = Icons.Rounded.Settings,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surface,
            )
        },
        label = {
            Text(
                text = "Logout",
                fontSize = 15.sp,
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.surface,
                fontFamily = Parkinsans
            )
        },
        selected = false,
        onClick = {
            authViewModel.signOut()
        },
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = Color.White,
            unselectedContainerColor = Color.White
        )
    )
}
