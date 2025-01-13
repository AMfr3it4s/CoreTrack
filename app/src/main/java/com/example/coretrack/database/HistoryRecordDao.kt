package com.example.coretrack.database

import androidx.room.*
import com.example.coretrack.model.HistoryRecord

@Dao
interface HistoryRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: HistoryRecord)

    @Query("SELECT * FROM history_records ORDER BY timestamp DESC")
    suspend fun getAllRecords(): List<HistoryRecord>

    @Delete
    suspend fun deleteRecord(record: HistoryRecord)
}
