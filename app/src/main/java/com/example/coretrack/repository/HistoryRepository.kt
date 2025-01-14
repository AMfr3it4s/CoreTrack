package com.example.coretrack.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.coretrack.database.HistoryRecordDao
import com.example.coretrack.model.HistoryRecord
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.FirebaseFirestore

class HistoryRepository(
    private val localDataSource: HistoryRecordDao,
    private val remoteDataSource: FirebaseFirestore,
    private val userId: String,
    private val context: Context
) {
    suspend fun saveRecord(record: HistoryRecord) {
        // Save to local database
        localDataSource.insert(HistoryRecord(bpm = record.bpm, timestamp = record.timestamp))

        // Save to Firebase if online
        if (isOnline()) {
            remoteDataSource.collection("users")
                .document(userId)
                .collection("history")
                .add(record)
        }
    }

    suspend fun deleteRecord(record: HistoryRecord) {
        try {
            localDataSource.deleteRecord(record)
            Log.d("Delete Record", "Attempting to delete record with ID: ${record.id} | ${record.bpm}, ${record.timestamp}")
            // Delete from Firebase if online
            if (isOnline()) {
                val remoteQuery = remoteDataSource.collection("users")
                    .document(userId)
                    .collection("history")
                    .whereEqualTo("id", record.id)
                    .get()
                    .await()

                for (document in remoteQuery.documents) {
                    document.reference.delete()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace() // Log deletion errors
        }
    }

    suspend fun getRecords(): List<HistoryRecord> {
        return if (isOnline()) {
            // Fetch from Firebase
            if (userId.isEmpty()) {
                throw IllegalArgumentException("User ID cannot be empty")
            }

            val result = remoteDataSource.collection("users")
                .document(userId)
                .collection("history")
                .get()
                .await()

            val records = result.documents.mapNotNull { it.toObject(HistoryRecord::class.java) }

            val localRecords = localDataSource.getAllRecords()
            for (record in localRecords) {
                if (!records.contains(record) ){
                    remoteDataSource.collection("users")
                        .document(userId)
                        .collection("history")
                        .add(record)
                }
            }

            records.forEach { remoteRecord ->
                if (!localRecords.contains(remoteRecord)){
                    localDataSource.insert(remoteRecord)
                }
            }

            localDataSource.getAllRecords().map { HistoryRecord(id = it.id, bpm = it.bpm, timestamp = it.timestamp) }
        } else {
            // Fetch from local database
            localDataSource.getAllRecords().map { HistoryRecord(id = it.id, bpm = it.bpm, timestamp = it.timestamp) }
        }
    }

    private fun isOnline(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
