package com.example.ui.viewmodel

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.auth.AuthHelper
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        val user = Firebase.auth.currentUser
        if (user != null) {
            _authState.value = AuthState.Authenticated(user.uid)
        }
    }

    fun attemptAutoSignIn(context: Context) {
        val credentialManager = CredentialManager.create(context)
        AuthHelper.attemptAutoSignIn(
            context = context,
            credentialManager = credentialManager,
            onAuthSuccess = {
                _authState.value = AuthState.Authenticated(Firebase.auth.currentUser?.uid ?: "")
            },
            onUnauthenticated = {
                _authState.value = AuthState.Unauthenticated
            },
            scope = viewModelScope
        )
    }

    fun signInWithGoogle(context: Context) {
        _authState.value = AuthState.Loading
        val credentialManager = CredentialManager.create(context)
        AuthHelper.onGoogleSignInClicked(
            context = context,
            credentialManager = credentialManager,
            onAuthSuccess = {
                _authState.value = AuthState.Authenticated(Firebase.auth.currentUser?.uid ?: "")
            },
            onAuthError = { error ->
                _authState.value = AuthState.Error(error)
            },
            scope = viewModelScope,
            onAuthCancelled = {
                _authState.value = AuthState.Unauthenticated
            }
        )
    }

    fun signOut(context: Context) {
        val credentialManager = CredentialManager.create(context)
        AuthHelper.signOut(
            context = context,
            credentialManager = credentialManager,
            onSignOutComplete = {
                _authState.value = AuthState.Unauthenticated
            },
            scope = viewModelScope
        )
    }
}

sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    object Unauthenticated : AuthState()
    data class Authenticated(val userId: String) : AuthState()
    data class Error(val message: String) : AuthState()
}
