package com.example.boxcounter.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.boxcounter.data.local.AppDataBase;
import com.example.boxcounter.data.local.BoxDao;
import com.example.boxcounter.model.BoxEntry;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BoxRepo {

//    private final BoxDao boxDao;
//    private final LiveData<List<BoxEntry>> allEntries;
//    private final ExecutorService executorService = Executors.newFixedThreadPool(4);
//
//    public BoxRepo(Application application) {
//        AppDataBase db = AppDataBase.getDataBase(application);
//        this.boxDao = db.boxDao();
//        this.allEntries = boxDao.getAllEntries();
//    }
//
//    /**
//     * Devuelve todos los registros de cajas almacenados
//     * @return LiveData con la lista de BoxEntry
//     */
//    public LiveData<List<BoxEntry>> getAllEntries(){
//        return allEntries;
//    }
//
//    /**
//     * Obtiene la sumatoria de cajas realizados desde una fecha especifica
//     * @param startOfDay milisegundos del inicio del día
//     * @return el total de la cajas
//     */
//    public LiveData<Integer> getTotalToday(long startOfDay){
//        return boxDao.getTotalQuantityToday(startOfDay);
//    }
//
//    /**
//     * Inserta un nuevo registro de cajas en segundo plano
//     * @param entry Objeto con la info a guardar
//     */
//    public void insert(BoxEntry entry){
//        executorService.execute(() -> boxDao.insert(entry));
//    }
//
//    /**
//     * Elimina el último registro ingresado
//     */
//    public void deleteLast(){
//        executorService.execute(boxDao::deleteLastEntry);
//    }
}
