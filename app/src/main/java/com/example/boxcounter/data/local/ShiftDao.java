package com.example.boxcounter.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.boxcounter.model.entity.Shift;

import java.util.List;

@Dao
public interface ShiftDao {

    @Insert
    long insert(Shift shift);

    @Update
    void update(Shift shift);

    @Query("SELECT * FROM shifts WHERE active = 1 LIMIT 1")
    Shift getActiveShift();

    @Query("SELECT * FROM shifts ORDER BY startTime DESC")
    LiveData<List<Shift>> getAllShifts();

    @Query("UPDATE shifts SET active = 0 WHERE active = 1")
    void closeAllActive();
}
