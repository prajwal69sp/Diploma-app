package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ui.components.NavCard
import com.example.ui.components.SectionHeader
import com.example.ui.navigation.Screen
import com.example.ui.viewmodel.MainViewModel

@Composable
fun HomeScreen(navController: NavController, mainViewModel: MainViewModel) {
    val profile by mainViewModel.profile.collectAsState()
    val appSettings by mainViewModel.appSettings.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            HomeHeader(profile?.displayName ?: "Student")
        }

        item {
            AiAssistantBanner { navController.navigate(Screen.AiAssistant) }
        }

        item {
            SectionHeader(title = "Continue Learning")
            if (profile?.enrolledBranchId != null) {
                NavCard(
                    title = "Your Branch: ${profile?.enrolledBranchId}",
                    subtitle = "Semester: ${profile?.enrolledSemester}",
                    icon = Icons.Default.MenuBook,
                    onClick = { 
                        navController.navigate(Screen.Subjects(profile!!.enrolledBranchId!!, profile!!.enrolledSemester!!))
                    }
                )
            } else {
                Text(
                    text = "Select a branch to start learning",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        item {
            SectionHeader(title = "Explore Branches") {
                TextButton(onClick = { navController.navigate(Screen.Branches) }) {
                    Text("See All")
                }
            }
        }

        // Mock branches for now until they load from repository in BranchesScreen
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                BranchQuickLink("E&C", Icons.Default.ElectricalServices, Color(0xFF2196F3)) {
                    navController.navigate(Screen.Semesters("ece", "Electronics & Communication"))
                }
                BranchQuickLink("AI & ML", Icons.Default.Psychology, Color(0xFF9C27B0)) {
                    navController.navigate(Screen.Semesters("aiml", "AI & Machine Learning"))
                }
                BranchQuickLink("CSE", Icons.Default.Computer, Color(0xFF4CAF50)) {
                    navController.navigate(Screen.Semesters("cse", "Computer Science"))
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            SectionHeader(title = "Support & About")
            NavCard(
                title = "About Us",
                subtitle = "Learn about the creator and platform",
                icon = Icons.Default.Info,
                onClick = { navController.navigate(Screen.AboutUs) }
            )
            NavCard(
                title = "Support Us",
                subtitle = "Help us grow the platform",
                icon = Icons.Default.Favorite,
                onClick = { navController.navigate(Screen.SupportUs) }
            )
        }
        
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun HomeHeader(name: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primaryContainer)
                )
            )
            .padding(24.dp)
    ) {
        Column(modifier = Modifier.align(Alignment.CenterStart)) {
            Text(
                text = "Hello, $name!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = "Ready to learn something new today?",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
            )
        }
        Box(
            modifier = Modifier
                .size(56.dp)
                .align(Alignment.TopEnd)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.White)
        }
    }
}

@Composable
fun AiAssistantBanner(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.AutoAwesome,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.tertiary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "AI Study Assistant",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Get step-by-step explanations for any topic.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun RowScope.BranchQuickLink(
    label: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .padding(8.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(color.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(32.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
    }
}
