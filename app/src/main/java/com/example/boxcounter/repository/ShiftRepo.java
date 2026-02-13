package com.example.boxcounter.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.boxcounter.data.local.AppDataBase;
import com.example.boxcounter.data.local.ShiftDao;
import com.example.boxcounter.model.entity.Shift;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShiftRepo {

    private final ShiftDao shiftDao;
    private final ExecutorService executor;
    private final MutableLiveData<Shift> activeShift = new MutableLiveData<>();

    public ShiftRepo(Context context) {
        AppDataBase db = AppDataBase.getDataBase(context);
        shiftDao = db.shiftDao();
        executor =  Executors.newSingleThreadExecutor();
    }

    public LiveData<Shift> getActiveShift(){
        executor.execute(() -> {
            Shift shift = shiftDao.getActiveShift();
            activeShift.postValue(shift);
        });
        return activeShift;
    }

    public void startNewShift(){
        executor.execute(() -> {
            shiftDao.closeAllActive();

            Shift newShift = new Shift();
            newShift.setQuantity(0);
            newShift.setStartTime(System.currentTimeMillis());
            newShift.setActive(true);
            newShift.setEndTime(null);

            shiftDao.insert(newShift);
            activeShift.postValue(newShift);
        });
    }

//    public LiveData<Shift> getActiveShift(){
//        executor.execute(() -> {
//            Shift shift = shiftDao.getActiveShift();
//            if (shift == null){
//                shiftDao.closeAllActive();
//
//                Shift newShift = new Shift();
//                newShift.setQuantity(0);
//                newShift.setStartTime(System.currentTimeMillis());
//                newShift.setActive(true);
//                newShift.setEndTime(null);
//
//                shiftDao.insert(newShift);
//                activeShift.postValue(newShift);
//            }else {
//                activeShift.postValue(shift);
//            }
//        });
//        return activeShift;
//    }

    public void update(Shift shift){
        executor.execute(() -> {
            shiftDao.update(shift);
            activeShift.postValue(shift);
        });
    }

    public void finishShift(Shift shift){
        executor.execute(() -> {
            shift.setActive(false);
            shift.setEndTime(System.currentTimeMillis());
            shiftDao.update(shift);

            activeShift.postValue(shift);
        });
    }

    public LiveData<List<Shift>> getHistory(){
        return shiftDao.getAllShifts();
    }
}
