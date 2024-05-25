package com.example.healthify.records

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(tableName = "records")
data class RecordData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val calorie: Double,
    val weight: Double,
    val timestamp: Long = System.currentTimeMillis()
)
