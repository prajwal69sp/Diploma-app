package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Branch
import com.example.data.model.LearningResource
import com.example.data.model.Subject
import com.example.data.repository.EducationRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AdminViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = EducationRepository(application)

    val allBranches: StateFlow<List<Branch>> = repository.observeBranches(onlyEnabled = false)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun saveBranch(branch: Branch) {
        viewModelScope.launch {
            repository.saveBranch(branch)
        }
    }

    fun saveSubject(subject: Subject) {
        viewModelScope.launch {
            repository.saveSubject(subject)
        }
    }

    fun saveResource(resource: LearningResource) {
        viewModelScope.launch {
            repository.saveResource(resource)
        }
    }

    fun seedSampleData() {
        viewModelScope.launch {
            repository.seedInitialData()
        }
    }
}
