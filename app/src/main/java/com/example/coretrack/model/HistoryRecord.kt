package com.example.coretrack.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@IgnoreExtraProperties
@Entity(tableName = "history_records")
data class HistoryRecord (
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @PropertyName("bpm") var bpm: Int = 0,
    @PropertyName("timestamp") var timestamp: Long = 0L,
)