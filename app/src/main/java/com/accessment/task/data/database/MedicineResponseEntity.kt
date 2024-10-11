package com.accessment.task.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicine_response")
data class MedicineResponseEntity(
    @PrimaryKey val id: Long = 1,
    val responseData: String
)