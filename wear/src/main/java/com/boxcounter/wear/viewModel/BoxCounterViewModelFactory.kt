package com.boxcounter.wear.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.boxcounter.wear.repository.ShiftRepo

class BoxCounterViewModelFactory(private val repo: ShiftRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BoxCounterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BoxCounterViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}