package com.accessment.task.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "med_response")
data class  AppDatabase(
    @PrimaryKey val id: Long = 1,
    val responseData: String
)