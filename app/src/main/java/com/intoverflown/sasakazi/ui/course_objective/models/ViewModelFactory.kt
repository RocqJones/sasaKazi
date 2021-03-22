package com.intoverflown.sasakazi.ui.course_objective.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intoverflown.sasakazi.data.Repository

class ViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ViewModel(repository) as T
    }
}