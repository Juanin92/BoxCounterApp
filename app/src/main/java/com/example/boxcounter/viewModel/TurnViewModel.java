package com.example.boxcounter.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.boxcounter.model.entity.Turn;
import com.example.boxcounter.repository.TurnRepo;
import com.example.boxcounter.validation.TurnValidator;

import java.util.List;
import java.util.function.Consumer;

public class TurnViewModel extends AndroidViewModel {

    private final TurnRepo repo;
    private final LiveData<Turn> activeTurn;
    private final LiveData<List<Turn>> history;
    private final TurnValidator validator;

    public TurnViewModel(@NonNull Application application) {
        super(application);

        repo = new TurnRepo(application);
        validator = new TurnValidator();

        activeTurn = repo.getActiveTurn();
        history = repo.getHistory();
    }

    //Test Constructor
    TurnViewModel(@NonNull Application application, TurnRepo repo, TurnValidator validator) {
        super(application);

        this.repo = repo;
        this.validator = validator;

        this.activeTurn = repo.getActiveTurn();
        this.history = repo.getHistory();
    }

    public LiveData<Turn> getActiveTurn() {
        return activeTurn;
    }

    public LiveData<List<Turn>> getHistory() {
        return history;
    }

    public void increment(){
       currentTurn(turn -> {
           turn.setQuantity(turn.getQuantity() + 1);
           validator.validateTurn(turn);
           repo.update(turn);
       });
    }

    public void decrement(){
        currentTurn(turn -> {
            if (turn.getQuantity() == 0) return;
            turn.setQuantity(turn.getQuantity() - 1);
            validator.validateTurn(turn);
            repo.update(turn);
        });
    }

    public void updateManuallyQuantity(int updateQuantity){
        currentTurn(turn -> {
            validator.validateManualInput(updateQuantity);
            turn.setQuantity(updateQuantity);

            validator.validateTurn(turn);
            repo.update(turn);
        });
    }

    public void updateIncrementManuallyQuantity(int newQuantity){
        currentTurn(turn -> {
            validator.validateManualInput(newQuantity);
            turn.setQuantity(turn.getQuantity() + newQuantity);

            validator.validateTurn(turn);
            repo.update(turn);
        });
    }

    public void finish(){
        currentTurn(turn -> {
            validator.validateTurn(turn);
            repo.finishTurn(turn);
        });
    }

    private void currentTurn(Consumer<Turn> action){
        Turn turn = activeTurn.getValue();
        if (turn == null) return;

        action.accept(turn);
    }
}
