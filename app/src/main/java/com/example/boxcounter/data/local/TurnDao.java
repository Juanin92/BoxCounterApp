package com.example.boxcounter.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.boxcounter.model.entity.Turn;

import java.util.List;

@Dao
public interface TurnDao {

    @Insert
    long insert(Turn turn);

    @Update
    void update(Turn turn);

    @Query("SELECT * FROM turns WHERE active = 1 LIMIT 1")
    Turn getActiveTurn();

    @Query("SELECT * FROM turns ORDER BY startTime DESC")
    LiveData<List<Turn>> getAllTurns();

    @Query("UPDATE turns SET active = 0 WHERE active = 1")
    void closeAllActive();
}
