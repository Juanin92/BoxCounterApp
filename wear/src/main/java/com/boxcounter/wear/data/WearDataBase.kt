package com.boxcounter.wear.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.boxcounter.core.entity.Shift

@Database(entities = [Shift::class], version = 1, exportSchema = false)
abstract class WearDataBase : RoomDatabase() {

    abstract fun shiftDao(): ShiftDao
}