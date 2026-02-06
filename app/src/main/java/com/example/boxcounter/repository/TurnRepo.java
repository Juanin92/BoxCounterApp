package com.example.boxcounter.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.boxcounter.data.local.AppDataBase;
import com.example.boxcounter.data.local.TurnDao;
import com.example.boxcounter.model.entity.Turn;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TurnRepo {

    private final TurnDao turnDao;
    private final ExecutorService executor;
    private final MutableLiveData<Turn> activeTurn = new MutableLiveData<>();

    public TurnRepo(Context context) {
        AppDataBase db = AppDataBase.getDataBase(context);
        turnDao = db.turnDao();
        executor =  Executors.newSingleThreadExecutor();
    }

    public LiveData<Turn> getActiveTurn(){
        executor.execute(() -> {
            Turn turn = turnDao.getActiveTurn();
            if (turn == null){
                turnDao.closeAllActive();

                Turn newTurn = new Turn();
                newTurn.setQuantity(0);
                newTurn.setStartTime(System.currentTimeMillis());
                newTurn.setActive(true);
                newTurn.setEndTime(null);

                turnDao.insert(newTurn);
                activeTurn.postValue(newTurn);
            }else {
                activeTurn.postValue(turn);
            }
        });
        return activeTurn;
    }

    public void update(Turn turn){
        executor.execute(() -> {
            turnDao.update(turn);
            activeTurn.postValue(turn);
        });
    }

    public void finishTurn(Turn turn){
        executor.execute(() -> {
            turn.setActive(false);
            turn.setEndTime(System.currentTimeMillis());
            turnDao.update(turn);

            activeTurn.postValue(turn);
        });
    }

    public LiveData<List<Turn>> getHistory(){
        return turnDao.getAllTurns();
    }
}
