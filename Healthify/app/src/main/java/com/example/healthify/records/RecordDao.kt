package com.example.healthify.records

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecordDao {
    @Insert
    suspend fun insert(record: RecordData)

    @Query("SELECT * FROM records")
    suspend fun getAllRecords(): List<RecordData>
}
