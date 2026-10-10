package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AppSettings
import com.example.data.model.Profile
import com.example.data.repository.EducationRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = EducationRepository(application)

    val profile: StateFlow<Profile?> = repository.observeProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val appSettings: StateFlow<AppSettings> = repository.observeAppSettings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppSettings())

    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin.asStateFlow()

    init {
        checkAdminStatus()
    }

    fun checkAdminStatus() {
        viewModelScope.launch {
            _isAdmin.value = repository.isAdmin()
        }
    }

    fun updateEnrolledBranch(branchId: String, semester: String) {
        viewModelScope.launch {
            profile.value?.let { currentProfile ->
                repository.updateProfile(currentProfile.copy(
                    enrolledBranchId = branchId,
                    enrolledSemester = semester
                ))
            }
        }
    }
}
