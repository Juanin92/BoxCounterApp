package com.boxcounter.wear.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.boxcounter.core.entity.Shift

@Dao
interface ShiftDao {

    @Query("SELECT * FROM shifts WHERE active = 1 LIMIT 1")
    suspend fun getActiveShift(): Shift?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShift(shift: Shift)

    @Update
    suspend fun updateShift(shift: Shift)

    @Query("DELETE FROM shifts")
    suspend fun deleteAll()
}