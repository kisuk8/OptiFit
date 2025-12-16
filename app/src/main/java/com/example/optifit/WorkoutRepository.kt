package com.example.optifit
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue


object WorkoutRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun saveWorkout(
        workoutName: String,
        durationSeconds: Long,
        rating: Int,
        feedback: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val userId = auth.currentUser?.uid
            ?: return onError(Exception("User not logged in"))

        val workoutData = hashMapOf(
            "workoutName" to workoutName,
            "durationSeconds" to durationSeconds,
            "rating" to rating,
            "feedback" to feedback
        )

        firestore
            .collection("users")
            .document(userId)
            .collection("workouts")
            .add(workoutData)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }
}