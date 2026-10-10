package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.data.model.Branch
import com.example.ui.components.NavCard
import com.example.ui.navigation.Screen
import com.example.ui.viewmodel.AdminViewModel
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    adminViewModel: AdminViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Admin Dashboard") })
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            item {
                Text(
                    text = "Content Management",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
            item {
                NavCard(
                    title = "Manage Branches",
                    subtitle = "Add, edit, or disable branches",
                    icon = Icons.Default.Category,
                    onClick = { navController.navigate(Screen.AdminBranches) }
                )
            }
            item {
                NavCard(
                    title = "App Settings",
                    subtitle = "Update app info and AI settings",
                    icon = Icons.Default.Settings,
                    onClick = { /* Navigate to app settings */ }
                )
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { adminViewModel.seedSampleData() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Seed Sample Data")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminBranchesScreen(
    navController: NavController,
    adminViewModel: AdminViewModel = viewModel()
) {
    val branches by adminViewModel.allBranches.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Branches") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Branch")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(branches) { branch ->
                ListItem(
                    headlineContent = { Text(branch.name) },
                    supportingContent = { Text("${branch.semesterCount} Semesters") },
                    trailingContent = {
                        Switch(
                            checked = branch.isEnabled,
                            onCheckedChange = { 
                                adminViewModel.saveBranch(branch.copy(isEnabled = it))
                            }
                        )
                    }
                )
                HorizontalDivider()
            }
        }

        if (showAddDialog) {
            AddBranchDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { adminViewModel.saveBranch(it) }
            )
        }
    }
}

@Composable
fun AddBranchDialog(onDismiss: () -> Unit, onConfirm: (Branch) -> Unit) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Branch") },
        text = {
            Column {
                TextField(value = name, onValueChange = { name = it }, label = { Text("Branch Name") })
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm(Branch(name = name, description = description))
                onDismiss()
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
