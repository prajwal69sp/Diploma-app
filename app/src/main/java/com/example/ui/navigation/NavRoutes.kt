package com.example.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable object Home : Screen
    @Serializable object Branches : Screen
    @Serializable data class Semesters(val branchId: String, val branchName: String) : Screen
    @Serializable data class Subjects(val branchId: String, val semester: String) : Screen
    @Serializable data class SubjectDetail(val subjectId: String, val subjectName: String) : Screen
    @Serializable object AiAssistant : Screen
    @Serializable object Bookmarks : Screen
    @Serializable object Profile : Screen
    @Serializable object AboutUs : Screen
    @Serializable object SupportUs : Screen
    @Serializable object AdminDashboard : Screen
    @Serializable object AdminBranches : Screen
    @Serializable data class AdminSubjects(val branchId: String) : Screen
    @Serializable data class AdminResources(val subjectId: String) : Screen
}
