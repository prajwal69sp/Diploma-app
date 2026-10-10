package com.example.util

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestoreException

enum class OperationType {
    CREATE, READ, UPDATE, DELETE, LIST
}

fun handleFirestoreError(exception: Exception, operation: OperationType, path: String) {
    val message = when (exception) {
        is FirebaseFirestoreException -> {
            "Firestore error [${exception.code}] during $operation at $path: ${exception.message}"
        }
        else -> "Unexpected error during $operation at $path: ${exception.message}"
    }
    Log.e("FirestoreError", message, exception)
}
