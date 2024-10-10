package com.accessment.task.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.accessment.task.data.models.MedicineResponse

@Dao
interface MedResponseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) // Replace the existing response
    suspend fun insertResponse(apiResponse: MedicineResponse)

    @Query("SELECT * FROM med_response LIMIT 1") // Fetch only the single response
    suspend fun getResponse(): MedicineResponse?

    @Query("DELETE FROM med_response")
    suspend fun deleteAllResponses() // clear the table if needed
}