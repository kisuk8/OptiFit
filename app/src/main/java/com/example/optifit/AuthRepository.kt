package com.example.optifit

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await


object AuthRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val currentUser get() = auth.currentUser

    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signup(email: String, password: String): Result<Unit> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        auth.signOut()
    }
}