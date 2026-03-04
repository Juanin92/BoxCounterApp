package com.example.boxcounter.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class BoxCounterViewModel : ViewModel(){
    private val _count = mutableStateOf(0)

    val count: State<Int> = _count

    fun increment(){
        _count.value++
    }

    fun decrement(){
        if (_count.value > 0){
            _count.value--
        }
    }
}