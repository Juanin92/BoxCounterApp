package com.example.boxcounter.utils;

import android.content.Context;

import com.example.boxcounter.model.entity.Shift;
import com.example.boxcounter.repository.ShiftRepo;
import com.example.boxcounter.validation.ShiftValidator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShiftLogic {

    private final ShiftRepo repo;
    private final ShiftValidator validator;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public ShiftLogic(ShiftRepo repo) {
        this.repo = repo;
        this.validator = new ShiftValidator();
    }

    public void increment(){
        executor.execute(() -> {
            Shift currentShift = repo.getActiveShiftSync();
            validator.validateShiftExists(currentShift);
            currentShift.setQuantity(currentShift.getQuantity() + 1);
            validator.validateShift(currentShift);
            repo.update(currentShift);
        });
    }

    public void decrement(){
        executor.execute(() -> {
            Shift currentShift = repo.getActiveShiftSync();
            validator.validateShiftExists(currentShift);
            if (currentShift.getQuantity() > 0) {
                currentShift.setQuantity(currentShift.getQuantity() - 1);
                validator.validateShift(currentShift);
                repo.update(currentShift);
            }
        });
    }
}
