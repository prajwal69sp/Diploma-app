package com.example.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class Profile(
    @DocumentId val userId: String = "",
    val email: String = "",
    val displayName: String = "",
    val photoUrl: String? = null,
    val role: String = "student",
    val enrolledBranchId: String? = null,
    val enrolledSemester: String? = null,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class Branch(
    @DocumentId val id: String = "",
    val name: String = "",
    val description: String = "",
    val iconName: String = "school",
    val displayOrder: Int = 0,
    val isEnabled: Boolean = true,
    val semesterCount: Int = 6,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class Subject(
    @DocumentId val id: String = "",
    val branchId: String = "",
    val semester: String = "I",
    val name: String = "",
    val code: String = "",
    val description: String = "",
    val displayOrder: Int = 0,
    val isPublished: Boolean = true,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class LearningResource(
    @DocumentId val id: String = "",
    val subjectId: String = "",
    val unitId: String? = null,
    val type: String = "notes", // notes, paper, video, quiz, important
    val title: String = "",
    val description: String = "",
    val fileUrl: String = "",
    val youtubeUrl: String? = null,
    val displayOrder: Int = 0,
    val isPublished: Boolean = true,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class Announcement(
    @DocumentId val id: String = "",
    val title: String = "",
    val message: String = "",
    @ServerTimestamp val timestamp: Timestamp? = null,
    val isPublished: Boolean = true
)

@IgnoreExtraProperties
data class AppSettings(
    @DocumentId val id: String = "main",
    val appName: String = "DiplomaHub AI",
    val tagline: String = "One App. Every Branch. Every Semester.",
    val creatorName: String = "Prajwal KM",
    val creatorBio: String = "Prajwal KM is the creator of the DiplomaHub AI learning platform...",
    val creatorEducation: String = "Diploma in E&C Engineering at GPT, K. R. Pet.",
    val youtubeUrl: String = "https://www.youtube.com/@HackBitsKannada",
    val instagramUrl: String = "https://www.instagram.com/prajwal_km_/",
    val supportEmail: String = "prajwalmarigowda69@gmail.com",
    val aiEnabled: Boolean = true
)
