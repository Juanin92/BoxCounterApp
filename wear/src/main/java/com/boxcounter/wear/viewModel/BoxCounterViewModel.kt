package com.boxcounter.wear.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boxcounter.core.entity.Shift
import com.boxcounter.wear.data.ShiftDao
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.boxcounter.wear.repository.ShiftRepo
import com.boxcounter.wear.util.copy

class BoxCounterViewModel(private val repo: ShiftRepo) : ViewModel(){
    var currentShift by mutableStateOf<Shift?>(null)
        private set

    init {
        loadActiveShift()
    }

    private fun loadActiveShift(){
        viewModelScope.launch {
            currentShift = repo.getActiveShift()
        }
    }

    fun increment() {
        val shift = currentShift ?: return
        val updateShift = shift.copy(quantity = shift.quantity + 1)
        currentShift = updateShift

        viewModelScope.launch {
            repo.updateLocalShift(updateShift)
        }
    }

    fun decrement() {
        val shift = currentShift ?: return
        val updateShift = if(shift.quantity > 0){
            shift.copy(quantity = shift.quantity - 1)
        } else {
            shift
        }
        currentShift = updateShift

        viewModelScope.launch {
            repo.updateLocalShift(updateShift)
        }
    }
}