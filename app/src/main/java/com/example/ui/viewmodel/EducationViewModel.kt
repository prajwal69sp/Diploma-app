package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Branch
import com.example.data.model.LearningResource
import com.example.data.model.Subject
import com.example.data.repository.EducationRepository
import kotlinx.coroutines.flow.*

class EducationViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = EducationRepository(application)

    val branches: StateFlow<List<Branch>> = repository.observeBranches()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun getSubjects(branchId: String, semester: String): Flow<List<Subject>> {
        return repository.observeSubjects(branchId, semester)
    }

    fun getResources(subjectId: String, type: String? = null): Flow<List<LearningResource>> {
        return repository.observeResources(subjectId, type)
    }
}
