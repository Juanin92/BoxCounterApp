package com.example.boxcounter.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.boxcounter.model.BoxEntry;

import java.util.List;

@Dao
public interface BoxDao {

    @Insert
    void insert(BoxEntry entry);

    @Query("SELECT SUM(quantity) FROM boxes WHERE timestamp >= :startOfDay")
    LiveData<Integer> getTotalQuantityToday(long startOfDay);

    @Query("SELECT * FROM boxes ORDER BY timestamp DESC")
    LiveData<List<BoxEntry>> getAllEntries();

    @Query("DELETE FROM boxes WHERE id = (SELECT MAX(id) FROM boxes)")
    void deleteLastEntry();
}
