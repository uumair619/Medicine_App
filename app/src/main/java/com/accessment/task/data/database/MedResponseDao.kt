package com.accessment.task.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MedResponseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResponse(medicineResponseEntity: MedicineResponseEntity)

    @Query("SELECT responseData FROM medicine_response WHERE id = 1 LIMIT 1")
    suspend fun getResponse(): String?

    @Query("DELETE FROM medicine_response")
    suspend fun deleteAllResponses()
}