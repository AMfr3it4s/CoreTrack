package com.example.coretrack.pages

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coretrack.camera.CameraPreviewWithAnalysis
import com.example.coretrack.database.AppDatabase
import com.example.coretrack.model.HistoryRecord
import com.example.coretrack.repository.HistoryRepository
import com.example.coretrack.ui.components.HeartRateChart
import com.example.coretrack.ui.components.HistoryCard
import com.example.coretrack.ui.theme.Parkinsans
import com.example.coretrack.utils.calculateBPM
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*

@Composable
fun CameraPermissionRequest(
    content: @Composable () -> Unit
) {
    var permissionGranted by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult (
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            permissionGranted = isGranted
        }
    )

    LaunchedEffect(Unit) {
        launcher.launch(Manifest.permission.CAMERA)
    }

    if (permissionGranted) {
        content()
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Camera permission is required to measure heart rate.")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RestrictedApi")
@Composable
fun HeartPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    userId: String,
) {
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val repository = remember {
        HistoryRepository(
            localDataSource = database.historyRecordDao(),
            remoteDataSource = FirebaseFirestore.getInstance(),
            userId = userId,
            context = context
        )
    }
    // Store real-time data points
    val sensorData = remember { mutableStateListOf<Pair<Long, Double>>() }
    // Store heart rate history
    val history = remember { mutableStateListOf<HistoryRecord>() }

    LaunchedEffect(Unit) {
        val records = repository.getRecords()
        history.addAll(records)
    }

    // Toggles measurement
    val isMeasuring = remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Control whether we show the history overlay
    val showHistoryPage = remember { mutableStateOf(false) }

    if (showHistoryPage.value) {
        // Show the history page
        HistoryPageInHeart(
            onBack = { showHistoryPage.value = false }, // tapping arrow sets this false
            history = history,
            coroutineScope = coroutineScope,
            onDeleteRecord = { record ->
                CoroutineScope(Dispatchers.IO).launch {
                    withContext(Dispatchers.Main) {
                        history.remove(record) // Reflect changes in UI
                    }
                    repository.deleteRecord(record)
                }
            }
        )
    } else {
        // Show the main heart layout
        HeartMainLayout(
            isMeasuring = isMeasuring,
            sensorData = sensorData,
            showHistoryPage = showHistoryPage,
            snackbarHostState = snackbarHostState,
            coroutineScope = coroutineScope,
            onSaveRecord = { record ->
                CoroutineScope(Dispatchers.IO).launch {
                    // Save the record to the repository
                    repository.saveRecord(record)
                    withContext(Dispatchers.Main) {
                        // Update the UI with the new record
                        history.add(record)
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeartMainLayout(
    isMeasuring: MutableState<Boolean>,
    sensorData: SnapshotStateList<Pair<Long, Double>>,
    showHistoryPage: MutableState<Boolean>,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    onSaveRecord: suspend (HistoryRecord) -> Unit,
) {
    CameraPermissionRequest {
        Scaffold(
            topBar = {
                if(!isMeasuring.value)
                {
                    TopAppBar(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clip(RoundedCornerShape(100.dp)),
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                        ),
                        windowInsets = WindowInsets(top=0.dp),
                        title = { Text(
                            text = "Heart Rate Monitor",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 17.sp,
                            fontFamily = Parkinsans,

                            ) },
                        actions = {
                            IconButton(onClick = { showHistoryPage.value = true }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.List,
                                    contentDescription = "Show history",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .size(27.dp)

                                )
                            }
                        }
                    )
                }
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            }
        ) { innerPadding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (!isMeasuring.value) {
                        // Show a big round button with "Start"
                        Spacer(modifier = Modifier.weight(1f))

                        Button(
                            onClick = {
                                sensorData.clear()
                                isMeasuring.value = true
                                // Start 15s measurement
                                coroutineScope.launch {
                                    delay(10_000)
                                    if (isMeasuring.value) {
                                        isMeasuring.value = false
                                        val bpm = calculateBPM(sensorData)
                                        if (bpm > 0) {
                                            val record = HistoryRecord(
                                                bpm = bpm,
                                                timestamp = System.currentTimeMillis(),
                                            )
                                            coroutineScope.launch {
                                                onSaveRecord(record)
                                            }
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("Heart Beat Rate: $bpm bpm")
                                            }
                                        } else {
                                            snackbarHostState.showSnackbar("Failed to calculate BPM")
                                        }
                                    }
                                }
                            },
                            shape = CircleShape,
                            modifier = Modifier.size(180.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = "Favorite",
                                    modifier = Modifier.size(40.dp),
                                    tint = MaterialTheme.colorScheme.tertiary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Start", fontFamily = Parkinsans, fontSize = 18.sp)
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                    } else {
                        // Show camera preview, chart, stop button, etc.
                        Spacer(modifier = Modifier.height(16.dp))

                        CameraPreviewWithAnalysis(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            enableTorch = true,
                            onImageAvailable = { avgRed ->
                                val now = System.currentTimeMillis()
                                sensorData.add(now to avgRed)
                                // Keep only a few seconds of data if you want
                                if (sensorData.size > 300) {
                                    sensorData.removeAt(0)
                                }
                            }
                        )

                        if (sensorData.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(100.dp))
                            HeartRateChart(
                                data = sensorData.map { it.second },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .padding(12.dp),
                                currentBPM = calculateBPM(sensorData)
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))  // push button bottom

                        Button(
                            onClick = {
                                // Stop measurement
                                isMeasuring.value = false
                                val bpm = calculateBPM(sensorData)
                                if (bpm > 0) {
                                    val record = HistoryRecord(
                                        bpm = bpm,
                                        timestamp = System.currentTimeMillis(),
                                    )
                                    coroutineScope.launch {
                                        onSaveRecord(record)
                                    }
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Heart Beat Rate: $bpm bpm")
                                    }
                                } else {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Failed to calculate BPM")
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.tertiary,
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text("Stop Measurement", fontFamily = Parkinsans, color = MaterialTheme.colorScheme.primary)
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryPageInHeart(
    onBack: () -> Unit,
    history: List<HistoryRecord>,
    coroutineScope: CoroutineScope,
    onDeleteRecord: suspend (HistoryRecord) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(50.dp)),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                windowInsets = WindowInsets(top = 0.dp)
                ,
                title = { Text("History", fontFamily = Parkinsans, color = MaterialTheme.colorScheme.primary)},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(history) { record ->
                    HistoryCard(
                        record,
                        onDelete = {
                            coroutineScope.launch {
                                onDeleteRecord(record)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TopBar(modifier: Modifier = Modifier) {

}