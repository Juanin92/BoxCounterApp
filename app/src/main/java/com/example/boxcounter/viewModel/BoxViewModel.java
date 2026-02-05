package com.example.boxcounter.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.boxcounter.model.BoxEntry;
import com.example.boxcounter.repository.BoxRepo;

import java.util.Calendar;

public class BoxViewModel extends AndroidViewModel {

//    private final BoxRepo boxRepo;
//    private final LiveData<Integer> totalToday;

    private static final int DAILY_TARGET = 100;

    public BoxViewModel(@NonNull Application application) {
        super(application);
//        boxRepo = new BoxRepo(application);

        long startToday = getStartOfDayMillis();
//        totalToday = boxRepo.getTotalToday(startToday);
    }

    /**
     * Proporciona el total de cajas acumuladas desde el inicio del día
     * @return el conteo entero actualizado automaticamente.
     */
    public LiveData<Integer> getTotalToday(){
        return null;
//        return totalToday;
    }

    /**
     * Proporciona la meta de cajas establecidas por turno.
     * @return valor de la meta.
     */
    public int getDailyTarget(){
        return DAILY_TARGET;
    }

    /**
     * Registra una nueva unidad de caja en el sistema.
     * la cantidad de cajas es 1 por cada incremento.
     */
    public void addBox(){
        BoxEntry entry = new BoxEntry(null,System.currentTimeMillis(),1);
//        boxRepo.insert(entry);
    }

    /**
     * Eliminación del ultimo registro de caja realizada.
     */
    public void undoLast(){
//        boxRepo.deleteLast();
    }

    /**
     * Calcula los milisegundos correspondiente del día actual.
     * @return Representación en milisegundos del inicio del día.
     */
    private long getStartOfDayMillis(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }
}
