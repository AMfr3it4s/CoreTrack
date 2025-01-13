package com.example.coretrack.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.coretrack.database.HistoryRecordDao
import com.example.coretrack.model.HistoryRecord
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

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
        // Delete from local database
        localDataSource.deleteRecord(record)

        // Delete from Firebase if online
        if (isOnline()) {
            remoteDataSource.collection("users")
                .document(userId)
                .collection("history")
                .whereEqualTo("id", record.id)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot.documents) {
                        document.reference.delete()
                    }
                }
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

            records
        } else {
            // Fetch from local database
            localDataSource.getAllRecords().map { HistoryRecord(bpm = it.bpm, timestamp = it.timestamp) }
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
