package com.example.data.repository

import android.content.Context
import com.example.R
import com.example.data.model.*
import com.example.util.OperationType
import com.example.util.handleFirestoreError
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

class EducationRepository(private val db: FirebaseFirestore) {

    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private val auth = Firebase.auth

    fun requireUserId(): String = auth.currentUser?.uid ?: error("User not signed in")

    // --- Profile ---
    fun observeProfile(): Flow<Profile?> {
        val uid = auth.currentUser?.uid ?: return flowOf(null)
        return db.collection("profiles").document(uid)
            .snapshots()
            .map { it.toObject(Profile::class.java) }
            .catch { e ->
                handleFirestoreError(e as Exception, OperationType.READ, "profiles/$uid")
                emit(null)
            }
    }

    suspend fun updateProfile(profile: Profile) {
        val uid = requireUserId()
        val docRef = db.collection("profiles").document(uid)
        try {
            docRef.set(profile.copy(userId = uid, updatedAt = null)).await()
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
            throw e
        }
    }

    suspend fun isAdmin(): Boolean {
        val uid = auth.currentUser?.uid ?: return false
        if (auth.currentUser?.email == "prajwalmarigowda69@gmail.com") return true
        return try {
            db.collection("admins").document(uid).get().await().exists()
        } catch (e: Exception) {
            false
        }
    }

    // --- Branches ---
    fun observeBranches(onlyEnabled: Boolean = true): Flow<List<Branch>> {
        var query: Query = db.collection("branches").orderBy("displayOrder")
        if (onlyEnabled) {
            query = query.whereEqualTo("isEnabled", true)
        }
        return query.snapshots()
            .map { it.toObjects(Branch::class.java) }
            .catch { e ->
                handleFirestoreError(e as Exception, OperationType.LIST, "branches")
                emit(emptyList())
            }
    }

    suspend fun saveBranch(branch: Branch) {
        val docRef = if (branch.id.isEmpty()) {
            db.collection("branches").document()
        } else {
            db.collection("branches").document(branch.id)
        }
        try {
            docRef.set(branch.copy(id = docRef.id, updatedAt = null)).await()
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
            throw e
        }
    }

    // --- Subjects ---
    fun observeSubjects(branchId: String, semester: String): Flow<List<Subject>> {
        return db.collection("subjects")
            .whereEqualTo("branchId", branchId)
            .whereEqualTo("semester", semester)
            .whereEqualTo("isPublished", true)
            .orderBy("displayOrder")
            .snapshots()
            .map { it.toObjects(Subject::class.java) }
            .catch { e ->
                handleFirestoreError(e as Exception, OperationType.LIST, "subjects")
                emit(emptyList())
            }
    }

    suspend fun saveSubject(subject: Subject) {
        val docRef = if (subject.id.isEmpty()) db.collection("subjects").document() else db.collection("subjects").document(subject.id)
        docRef.set(subject.copy(id = docRef.id, updatedAt = null)).await()
    }

    // --- Resources ---
    fun observeResources(subjectId: String, type: String? = null): Flow<List<LearningResource>> {
        var query: Query = db.collection("resources")
            .whereEqualTo("subjectId", subjectId)
            .whereEqualTo("isPublished", true)
        
        if (type != null) {
            query = query.whereEqualTo("type", type)
        }
        
        return query.orderBy("displayOrder").snapshots()
            .map { it.toObjects(LearningResource::class.java) }
            .catch { e ->
                handleFirestoreError(e as Exception, OperationType.LIST, "resources")
                emit(emptyList())
            }
    }

    suspend fun saveResource(resource: LearningResource) {
        val docRef = if (resource.id.isEmpty()) db.collection("resources").document() else db.collection("resources").document(resource.id)
        docRef.set(resource.copy(id = docRef.id, updatedAt = null)).await()
    }

    // --- App Settings ---
    fun observeAppSettings(): Flow<AppSettings> {
        return db.collection("app_settings").document("main")
            .snapshots()
            .map { it.toObject(AppSettings::class.java) ?: AppSettings() }
            .catch { e ->
                handleFirestoreError(e as Exception, OperationType.READ, "app_settings/main")
                emit(AppSettings())
            }
    }

    suspend fun saveAppSettings(settings: AppSettings) {
        db.collection("app_settings").document("main").set(settings).await()
    }

    suspend fun seedInitialData() {
        val branches = listOf(
            Branch(id = "ece", name = "Electronics & Communication", description = "Hardware, circuits and signals", displayOrder = 1),
            Branch(id = "cse", name = "Computer Science", description = "Software, coding and systems", displayOrder = 2),
            Branch(id = "aiml", name = "AI & Machine Learning", description = "Intelligent systems and data", displayOrder = 3),
            Branch(id = "eee", name = "Electrical & Electronics", description = "Power and energy systems", displayOrder = 4)
        )
        branches.forEach { saveBranch(it) }

        // Sample Subjects for ECE III Sem
        val eceSubjects = listOf(
            Subject(id = "c_prog", branchId = "ece", semester = "III", name = "C Programming", code = "20EC31P", displayOrder = 1),
            Subject(id = "de2", branchId = "ece", semester = "III", name = "Digital Electronics II", code = "20EC32P", displayOrder = 2)
        )
        eceSubjects.forEach { saveSubject(it) }
        
        // Sample Resources
        saveResource(LearningResource(
            subjectId = "c_prog",
            type = "notes",
            title = "Introduction to C",
            description = "Basics of C programming language",
            fileUrl = "https://example.com/notes.pdf"
        ))
    }
}
