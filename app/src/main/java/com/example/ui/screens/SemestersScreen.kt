package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ui.components.NavCard
import com.example.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SemestersScreen(navController: NavController, branchId: String, branchName: String) {
    val semesters = listOf(
        "I Semester" to "I",
        "II Semester" to "II",
        "III Semester" to "III",
        "IV Semester" to "IV",
        "V Semester" to "V",
        "VI Semester" to "VI"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(branchName) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            item {
                Text(
                    text = "Select Semester",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }
            items(semesters) { (label, value) ->
                NavCard(
                    title = label,
                    icon = Icons.Default.School,
                    onClick = { 
                        navController.navigate(Screen.Subjects(branchId, value))
                    }
                )
            }
        }
    }
}
