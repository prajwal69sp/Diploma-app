package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.data.model.LearningResource
import com.example.ui.viewmodel.EducationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectLearningDashboard(
    navController: NavController,
    subjectId: String,
    subjectName: String,
    viewModel: EducationViewModel = viewModel()
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Notes", "Papers", "Videos", "Quiz")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(subjectName) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            ScrollableTabRow(selectedTabIndex = selectedTab, edgePadding = 16.dp) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }
            
            val type = when (selectedTab) {
                0 -> "notes"
                1 -> "paper"
                2 -> "video"
                3 -> "quiz"
                else -> "notes"
            }
            
            ResourceList(subjectId, type, viewModel)
        }
    }
}

@Composable
fun ResourceList(subjectId: String, type: String, viewModel: EducationViewModel) {
    val resources by viewModel.getResources(subjectId, type).collectAsState(initial = emptyList())

    if (resources.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No resources found.")
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(resources) { resource ->
                ResourceItem(resource)
            }
        }
    }
}

@Composable
fun ResourceItem(resource: LearningResource) {
    ListItem(
        headlineContent = { Text(resource.title) },
        supportingContent = { Text(resource.description) },
        leadingContent = {
            val icon = when (resource.type) {
                "notes" -> Icons.Default.Description
                "paper" -> Icons.Default.QuestionAnswer
                "video" -> Icons.Default.PlayCircle
                "quiz" -> Icons.Default.Quiz
                else -> Icons.Default.Article
            }
            Icon(icon, contentDescription = null)
        },
        trailingContent = {
            IconButton(onClick = { /* Handle download/open */ }) {
                Icon(Icons.Default.OpenInNew, contentDescription = "Open")
            }
        },
        modifier = Modifier.padding(horizontal = 8.dp)
    )
    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
}
