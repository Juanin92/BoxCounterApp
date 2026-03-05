package com.boxcounter.wear.data

import android.content.Context
import androidx.room.Room

object DataBaseProvider {

    private var INSTANCE: WearDataBase? = null

    fun getDataBase(context: Context) : WearDataBase{
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                WearDataBase::class.java,
                "box_counter_db"
            )
                .fallbackToDestructiveMigration()
                .build()

            INSTANCE = instance
            instance
        }
    }
}