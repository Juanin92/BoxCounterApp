package com.example.boxcounter.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.boxcounter.model.entity.Turn;
import com.example.boxcounter.repository.TurnRepo;

import java.util.List;

public class TurnViewModel extends AndroidViewModel {

    private final TurnRepo repo;
    private final LiveData<Turn> activeTurn;
    private final LiveData<List<Turn>> history;


    public TurnViewModel(@NonNull Application application) {
        super(application);

        repo = new TurnRepo(application);

        activeTurn = repo.getActiveTurn();
        history = repo.getHistory();
    }

    public LiveData<Turn> getActiveTurn() {
        return activeTurn;
    }

    public LiveData<List<Turn>> getHistory() {
        return history;
    }

    public void increment(){
        Turn turn = activeTurn.getValue();
        if (turn == null) return;

        turn.setQuantity(turn.getQuantity() + 1);
        repo.update(turn);
    }

    public void decrement(){
        Turn turn = activeTurn.getValue();
        if (turn == null) return;

        if (turn.getQuantity() > 0){
            turn.setQuantity(turn.getQuantity() - 1);
            repo.update(turn);
        }
    }

    public void updateManuallyQuantity(int newQuantity){

        if (newQuantity < 0) return;

        Turn turn = activeTurn.getValue();
        if (turn == null) return;

        turn.setQuantity(newQuantity);

        repo.update(turn);
    }


    public void finish(){
        Turn turn = activeTurn.getValue();

        if (turn == null) return;
        repo.finishTurn(turn);
    }
}
