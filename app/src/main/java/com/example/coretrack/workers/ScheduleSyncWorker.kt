package com.example.coretrack.workers

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

fun scheduleSyncWorker(context: Context, userId: String) {
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED) // Ensure work runs only when the network is available
        .build()

    val inputData = workDataOf("USER_ID" to userId)

    val syncWorkRequest = PeriodicWorkRequestBuilder<SyncWorker>(15, TimeUnit.MINUTES)
        .setConstraints(constraints)
        .setInputData(inputData)
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "SyncWorker",
        ExistingPeriodicWorkPolicy.KEEP,
        syncWorkRequest
    )
}
