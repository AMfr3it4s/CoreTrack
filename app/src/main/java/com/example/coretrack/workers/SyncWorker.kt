package com.example.coretrack.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.coretrack.database.AppDatabase
import com.example.coretrack.repository.HistoryRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SyncWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val userId = inputData.getString("USER_ID") ?: return Result.failure()

        val database = AppDatabase.getDatabase(context)
        val repository = HistoryRepository(
            localDataSource = database.historyRecordDao(),
            remoteDataSource = FirebaseFirestore.getInstance(),
            userId = userId,
            context = context
        )

        return withContext(Dispatchers.IO) {
            try {
                // Push unsynced local records to Firebase
                val localRecords = database.historyRecordDao().getAllRecords()
                for (record in localRecords) {
                    repository.saveRecord(record)
                }

                // Fetch new records from Firebase and update local database
                val remoteRecords = repository.getRecords()
                remoteRecords.forEach { remoteRecord ->
                    database.historyRecordDao().insert(remoteRecord)
                }

                Result.success()
            } catch (e: Exception) {
                e.printStackTrace()
                Result.failure()
            }
        }
    }
}

