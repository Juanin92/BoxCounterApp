package com.example.boxcounter.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.boxcounter.model.entity.Shift;
import com.example.boxcounter.repository.ShiftRepo;
import com.example.boxcounter.validation.ShiftValidator;

import java.util.List;
import java.util.function.Consumer;

public class ShiftViewModel extends AndroidViewModel {

    private final ShiftRepo repo;
    private final LiveData<Shift> activeShift;
    private final LiveData<List<Shift>> history;
    private final ShiftValidator validator;

    public ShiftViewModel(@NonNull Application application) {
        super(application);

        repo = new ShiftRepo(application);
        validator = new ShiftValidator();

        activeShift = repo.getActiveShift();
        history = repo.getHistory();
    }

    //Test Constructor
    ShiftViewModel(@NonNull Application application, ShiftRepo repo, ShiftValidator validator) {
        super(application);

        this.repo = repo;
        this.validator = validator;

        this.activeShift = repo.getActiveShift();
        this.history = repo.getHistory();
    }

    public LiveData<Shift> getActiveShift() {
        return activeShift;
    }

    public LiveData<List<Shift>> getHistory() {
        return history;
    }

    public void startNewShift() {
        repo.startNewShift();
    }

    public void increment(){
       currentShift(shift -> {
           shift.setQuantity(shift.getQuantity() + 1);
           validator.validateShift(shift);
           repo.update(shift);
       });
    }

    public void decrement(){
        currentShift(shift -> {
            if (shift.getQuantity() == 0) return;
            shift.setQuantity(shift.getQuantity() - 1);
            validator.validateShift(shift);
            repo.update(shift);
        });
    }

    public void updateManuallyQuantity(int updateQuantity){
        currentShift(shift -> {
            validator.validateManualInput(updateQuantity);
            shift.setQuantity(updateQuantity);

            validator.validateShift(shift);
            repo.update(shift);
        });
    }

    public void updateIncrementManuallyQuantity(int newQuantity){
        currentShift(shift -> {
            validator.validateManualInput(newQuantity);
            shift.setQuantity(shift.getQuantity() + newQuantity);

            validator.validateShift(shift);
            repo.update(shift);
        });
    }

    public void finish(){
        currentShift(shift -> {
            validator.validateShift(shift);
            repo.finishShift(shift);
        });
    }

    private void currentShift(Consumer<Shift> action){
        Shift shift = activeShift.getValue();
        if (shift == null) return;

        action.accept(shift);
    }
}
