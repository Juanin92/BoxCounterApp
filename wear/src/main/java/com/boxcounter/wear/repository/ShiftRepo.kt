package com.boxcounter.wear.repository

import com.boxcounter.core.entity.Shift
import com.boxcounter.wear.data.ShiftDao

class ShiftRepo(private val shiftDao: ShiftDao) {

    suspend fun getActiveShift(): Shift? = shiftDao.getActiveShift()

    suspend fun syncFromPhone(shift: Shift){
        shiftDao.insertShift(shift)
    }

    suspend fun updateLocalShift(shift: Shift){
        shiftDao.updateShift(shift)
    }

    suspend fun clearAllData(){
        shiftDao.deleteAll()
    }
}