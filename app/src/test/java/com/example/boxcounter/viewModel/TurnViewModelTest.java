package com.example.boxcounter.viewModel;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.boxcounter.model.entity.Turn;
import com.example.boxcounter.repository.TurnRepo;
import com.example.boxcounter.validation.TurnValidator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class TurnViewModelTest {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @Mock private TurnRepo repo;
    @Mock private TurnValidator validator;
    @Mock private Application application;
    private TurnViewModel turnViewModel;
    private Turn turn;
    private MutableLiveData<Turn> activeTurn;
    private MutableLiveData<List<Turn>> history;

    @Before
    public void setUp() {
        activeTurn = new MutableLiveData<>();
        history =  new MutableLiveData<>();
        when(repo.getActiveTurn()).thenReturn(activeTurn);
        when(repo.getHistory()).thenReturn(history);

        turnViewModel = new TurnViewModel(application, repo, validator);
        turn = new Turn(0,System.currentTimeMillis(),null, true);
    }

    //Prueba para aumentar cantidad de cajas
    @Test
    public void increment() {
        turn.setQuantity(5);
        activeTurn.setValue(turn);

        turnViewModel.increment();

        assertEquals(6, turn.getQuantity());

        verify(validator).validateTurn(turn);
        verify(repo).update(turn);
    }
    @Test
    public void increment_noActiveTurn() {
        activeTurn.setValue(null);

        turnViewModel.increment();

        verify(repo).getActiveTurn();
        verify(repo, never()).update(turn);
        verifyNoInteractions(validator);
    }

    //Prueba para reducir cantidad de cajas
    @Test
    public void decrement() {
        turn.setQuantity(5);
        activeTurn.setValue(turn);

        turnViewModel.decrement();

        assertEquals(4, turn.getQuantity());

        verify(validator).validateTurn(turn);
        verify(repo).update(turn);
    }
    @Test
    public void decrement_quantityIsZero() {
        activeTurn.setValue(turn);

        turnViewModel.decrement();

        assertEquals(0, turn.getQuantity());

        verify(validator, never()).validateTurn(any());
        verify(repo, never()).update(any());
    }

    //Prueba para ingreso manual de cantidad de cajas
    @Test
    public void updateManuallyQuantity() {
        turn.setQuantity(10);
        activeTurn.setValue(turn);

        turnViewModel.updateManuallyQuantity(30);

        assertEquals(30, turn.getQuantity());

        verify(validator).validateManualInput(30);
        verify(validator).validateTurn(turn);
        verify(repo).update(turn);
    }

    //Prueba para ingreso manual de incremento de cantidad de cajas
    @Test
    public void updateIncrementManuallyQuantity() {
        turn.setQuantity(15);
        activeTurn.setValue(turn);

        turnViewModel.updateIncrementManuallyQuantity(10);

        assertEquals(25, turn.getQuantity());

        verify(validator).validateManualInput(10);
        verify(validator).validateTurn(turn);
        verify(repo).update(turn);
    }

    //Prueba para terminar turno
    @Test
    public void finish() {
        activeTurn.setValue(turn);

        turnViewModel.finish();

        verify(validator).validateTurn(turn);
        verify(repo).finishTurn(turn);
    }
}