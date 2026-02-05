package com.example.boxcounter.validation;

import static org.junit.Assert.*;

import com.example.boxcounter.exceptions.ActiveTurnNotFoundException;
import com.example.boxcounter.exceptions.InvalidQuantityException;
import com.example.boxcounter.exceptions.InvalidTurnStateException;
import com.example.boxcounter.model.entity.Turn;

import org.junit.Before;
import org.junit.Test;

public class TurnValidatorTest {

    private TurnValidator validator;
    private Turn turn;

    @Before
    public void setUp(){
        validator =  new TurnValidator();
        turn = new Turn(100,System.currentTimeMillis(),System.currentTimeMillis(), false);
    }

    //Prueba para cuando un turno sea nulo
    @Test
    public void validateTurn_nullTurn(){
        ActiveTurnNotFoundException ex = assertThrows(ActiveTurnNotFoundException.class,
                () -> validator.validateTurnExists(null));
        assertEquals("Turno no existente", ex.getMessage());
    }

    //Prueba para validar que cantidad de cajas no sea menor a 0
    @Test
    public void validateQuantity_negativeValue(){
        turn.setQuantity(-1);
        InvalidQuantityException ex = assertThrows(InvalidQuantityException.class,
                () -> validator.validateTurn(turn));
        assertEquals("La cantidad no puede ser menor a 0", ex.getMessage());
    }

    //Prueba para validar que el registro de hora de inicio de turno no sea nulo
    @Test
    public void validateTimes_nullStartTime(){
        turn.setStartTime(null);
        InvalidTurnStateException ex = assertThrows(InvalidTurnStateException.class,
                () -> validator.validateTurn(turn));
        assertEquals("La hora inicial esta vacía", ex.getMessage());
    }

    //Prueba para validar que la hora de cierre de turno no sea anterior al comienzo de turno
    @Test
    public void validateTimes_invalidTime(){
        turn.setEndTime(System.currentTimeMillis() - 1000);
        InvalidTurnStateException ex = assertThrows(InvalidTurnStateException.class,
                () -> validator.validateTurn(turn));
        assertEquals("La hora de cierre no puede ser antes a la hora de inicio",
                ex.getMessage());
    }

    //Prueba cuando un valor de cantidad sea menor a 1 caja
    @Test
    public void validateManualInput_zeroValue(){
        InvalidQuantityException ex = assertThrows(InvalidQuantityException.class,
                () -> validator.validateManualInput(0));
        assertEquals("La cantidad debe ser desde una 1 caja", ex.getMessage());
    }

    //Prueba para validar que el monto de cantidad no sea mayor a 100 cajas
    @Test
    public void validateManualInput_overLimitValue(){
        InvalidQuantityException ex = assertThrows(InvalidQuantityException.class,
                () -> validator.validateManualInput(101));
        assertEquals("La cantidad debe exceder 100 cajas desde una sola vez",
                ex.getMessage());
    }
}