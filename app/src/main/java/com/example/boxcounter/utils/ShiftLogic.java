package com.example.boxcounter.utils;

import android.content.Context;

import com.example.boxcounter.model.entity.Shift;
import com.example.boxcounter.repository.ShiftRepo;
import com.example.boxcounter.validation.ShiftValidator;

public class ShiftLogic {

    private final ShiftRepo repo;
    private final ShiftValidator validator;

    public ShiftLogic(Context context) {
        this.repo = new ShiftRepo(context);
        this.validator = new ShiftValidator();
    }

    public void increment(){
        Shift currentShift = repo.getActiveShiftSync();
        validator.validateShiftExists(currentShift);
        currentShift.setQuantity(currentShift.getQuantity() + 1);
        validator.validateShift(currentShift);
        repo.update(currentShift);
    }

    public void decrement(){
        Shift currentShift = repo.getActiveShiftSync();
        validator.validateShiftExists(currentShift);
        if (currentShift.getQuantity() > 0) {
            currentShift.setQuantity(currentShift.getQuantity() - 1);
            validator.validateShift(currentShift);
            repo.update(currentShift);
        }
    }
}
