package com.example.boxcounter.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.boxcounter.model.BoxEntry;

@Database(entities = {BoxEntry.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    public abstract BoxDao boxDao();

    private static volatile AppDataBase INSTANCE;

    public static AppDataBase getDataBase(final Context context){
        if (INSTANCE == null){
            synchronized (AppDataBase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDataBase.class, "box_counter_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
