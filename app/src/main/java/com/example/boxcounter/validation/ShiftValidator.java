package com.example.boxcounter.validation;

import com.example.boxcounter.exceptions.ActiveShiftNotFoundException;
import com.example.boxcounter.exceptions.InvalidQuantityException;
import com.example.boxcounter.exceptions.InvalidShiftStateException;
import com.example.boxcounter.model.entity.Shift;

public class ShiftValidator {

    public void validateShift(Shift shift){
        validateQuantity(shift.getQuantity());
        validateTimes(shift);
    }

    public void validateManualInput(int quantity){
        if (quantity < 1){
            throw new InvalidQuantityException("La cantidad debe ser desde una 1 caja");
        }
        if (quantity > 100){
            throw new InvalidQuantityException("La cantidad debe exceder 100 cajas " +
                    "desde una sola vez");
        }
    }

    public void validateShiftExists(Shift shift){
        if (shift == null){
            throw new ActiveShiftNotFoundException("Turno no existente");
        }
    }

    private void validateQuantity(int quantity){
        if (quantity < 0){
            throw new InvalidQuantityException("La cantidad no puede ser menor a 0");
        }
    }

    private void validateTimes(Shift shift){

        if (shift.getStartTime() == null){
            throw new InvalidShiftStateException("La hora inicial esta vacía");
        }

        if (shift.getEndTime() != null && shift.getEndTime() < shift.getStartTime()){
            throw new InvalidShiftStateException("La hora de cierre no puede ser antes " +
                    "a la hora de inicio");
        }
    }
}
