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

import com.example.boxcounter.model.entity.Shift;
import com.example.boxcounter.repository.ShiftRepo;
import com.example.boxcounter.validation.ShiftValidator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ShiftViewModelTest {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @Mock private ShiftRepo repo;
    @Mock private ShiftValidator validator;
    @Mock private Application application;
    private ShiftViewModel shiftViewModel;
    private Shift shift;
    private MutableLiveData<Shift> activeShift;

    @Before
    public void setUp() {
        activeShift = new MutableLiveData<>();
        MutableLiveData<List<Shift>> history = new MutableLiveData<>();
        when(repo.getActiveShift()).thenReturn(activeShift);
        when(repo.getHistory()).thenReturn(history);

        shiftViewModel = new ShiftViewModel(application, repo, validator);
        shift = new Shift(0,System.currentTimeMillis(),null, true);
    }

    //Prueba para aumentar cantidad de cajas
    @Test
    public void increment() {
        shift.setQuantity(5);
        activeShift.setValue(shift);

        shiftViewModel.increment();

        assertEquals(6, shift.getQuantity());

        verify(validator).validateShift(shift);
        verify(repo).update(shift);
    }
    @Test
    public void increment_noActiveShift() {
        activeShift.setValue(null);

        shiftViewModel.increment();

        verify(repo).getActiveShift();
        verify(repo, never()).update(shift);
        verifyNoInteractions(validator);
    }

    //Prueba para reducir cantidad de cajas
    @Test
    public void decrement() {
        shift.setQuantity(5);
        activeShift.setValue(shift);

        shiftViewModel.decrement();

        assertEquals(4, shift.getQuantity());

        verify(validator).validateShift(shift);
        verify(repo).update(shift);
    }
    @Test
    public void decrement_quantityIsZero() {
        activeShift.setValue(shift);

        shiftViewModel.decrement();

        assertEquals(0, shift.getQuantity());

        verify(validator, never()).validateShift(any());
        verify(repo, never()).update(any());
    }

    //Prueba para ingreso manual de cantidad de cajas
    @Test
    public void updateManuallyQuantity() {
        shift.setQuantity(10);
        activeShift.setValue(shift);

        shiftViewModel.updateManuallyQuantity(30);

        assertEquals(30, shift.getQuantity());

        verify(validator).validateManualInput(30);
        verify(validator).validateShift(shift);
        verify(repo).update(shift);
    }

    //Prueba para ingreso manual de incremento de cantidad de cajas
    @Test
    public void updateIncrementManuallyQuantity() {
        shift.setQuantity(15);
        activeShift.setValue(shift);

        shiftViewModel.updateIncrementManuallyQuantity(10);

        assertEquals(25, shift.getQuantity());

        verify(validator).validateManualInput(10);
        verify(validator).validateShift(shift);
        verify(repo).update(shift);
    }

    //Prueba para terminar shifto
    @Test
    public void finish() {
        activeShift.setValue(shift);

        shiftViewModel.finish();

        verify(validator).validateShift(shift);
        verify(repo).finishShift(shift);
    }
}