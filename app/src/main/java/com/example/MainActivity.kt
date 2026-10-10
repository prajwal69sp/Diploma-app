package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.ui.screens.SignInScreen
import com.example.ui.theme.DiplomaHubAITheme
import com.example.ui.viewmodel.AuthState
import com.example.ui.viewmodel.AuthViewModel
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.navigation.AppNavigation
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            DiplomaHubAITheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppContent(authViewModel, mainViewModel)
                }
            }
        }
    }
}

@Composable
fun AppContent(authViewModel: AuthViewModel, mainViewModel: MainViewModel) {
    val authState by authViewModel.authState.collectAsState()
    
    when (val state = authState) {
        is AuthState.Authenticated -> {
            AppNavigation(authViewModel, mainViewModel)
        }
        else -> {
            SignInScreen(authViewModel)
        }
    }
}
