package com.example.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.ui.screens.*
import com.example.ui.viewmodel.*

@Composable
fun AppNavigation(authViewModel: AuthViewModel, mainViewModel: MainViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar(currentRoute)) {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home") },
                        selected = currentRoute?.contains("Home") == true,
                        onClick = { navController.navigate(Screen.Home) }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Search, contentDescription = "Explore") },
                        label = { Text("Explore") },
                        selected = currentRoute?.contains("Branches") == true,
                        onClick = { navController.navigate(Screen.Branches) }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.AutoAwesome, contentDescription = "AI Tutor") },
                        label = { Text("AI Tutor") },
                        selected = currentRoute?.contains("AiAssistant") == true,
                        onClick = { navController.navigate(Screen.AiAssistant) }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                        label = { Text("Profile") },
                        selected = currentRoute?.contains("Profile") == true,
                        onClick = { navController.navigate(Screen.Profile) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<Screen.Home> { HomeScreen(navController, mainViewModel) }
            composable<Screen.Branches> { BranchesScreen(navController) }
            composable<Screen.Semesters> { backStackEntry ->
                val semesters: Screen.Semesters = backStackEntry.toRoute()
                SemestersScreen(navController, semesters.branchId, semesters.branchName)
            }
            composable<Screen.Subjects> { backStackEntry ->
                val subjects: Screen.Subjects = backStackEntry.toRoute()
                SubjectsScreen(navController, subjects.branchId, subjects.semester)
            }
            composable<Screen.SubjectDetail> { backStackEntry ->
                val detail: Screen.SubjectDetail = backStackEntry.toRoute()
                SubjectLearningDashboard(navController, detail.subjectId, detail.subjectName)
            }
            composable<Screen.AiAssistant> { AiAssistantScreen(mainViewModel) }
            composable<Screen.Profile> { ProfileScreen(navController, authViewModel, mainViewModel) }
            composable<Screen.AboutUs> { AboutUsScreen(mainViewModel) }
            composable<Screen.SupportUs> { SupportUsScreen(mainViewModel) }
            
            // Admin Panel
            composable<Screen.AdminDashboard> { AdminDashboardScreen(navController, mainViewModel) }
            composable<Screen.AdminBranches> { AdminBranchesScreen(navController) }
            // Add more admin routes as needed
        }
    }
}

private fun shouldShowBottomBar(route: String?): Boolean {
    return route != null && (
        route.contains("Home") || 
        route.contains("Branches") || 
        route.contains("AiAssistant") || 
        route.contains("Profile")
    )
}
