package com.example.boxcounter.validation;

import com.example.boxcounter.exceptions.ActiveTurnNotFoundException;
import com.example.boxcounter.exceptions.InvalidQuantityException;
import com.example.boxcounter.exceptions.InvalidTurnStateException;
import com.example.boxcounter.model.entity.Turn;

public class TurnValidator {

    public void validateTurn(Turn turn){
        validateTurnExists(turn);
        validateManualInput(turn.getQuantity());
        validateQuantity(turn.getQuantity());
        validateTimes(turn);
    }

    private void validateTurnExists(Turn turn){
        if (turn == null){
            throw new ActiveTurnNotFoundException("Turno no existente");
        }
    }

    private void validateManualInput(int quantity){
        if (quantity < 1){
            throw new InvalidQuantityException("La cantidad debe ser desde una 1 caja");
        }
        if (quantity > 100){
            throw new InvalidQuantityException("La cantidad debe exceder 100 cajas " +
                    "desde una sola vez");
        }
    }

    private void validateQuantity(int quantity){
        if (quantity < 0){
            throw new InvalidQuantityException("La cantidad no puede ser menor a 0");
        }
    }

    private void validateTimes(Turn turn){

        if (turn.getStartTime() == null){
            throw new InvalidTurnStateException("La hora inicial esta vacía");
        }

        if (turn.getEndTime() != null && turn.getEndTime() < turn.getStartTime()){
            throw new InvalidTurnStateException("La hora de cierre no puede ser antes " +
                    "a la hora de inicio");
        }
    }
}
