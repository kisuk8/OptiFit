package com.example.optifit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuthException
import androidx.compose.runtime.*

class authViewModel : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        public set

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        isLoading = true
        errorMessage = null

        viewModelScope.launch {
            try {
                AuthRepository.login(email, password)
                onSuccess()
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun signup(email: String, password: String, onSuccess: () -> Unit) {
        isLoading = true
        errorMessage = null

        viewModelScope.launch {
            try {
                AuthRepository.signup(email, password)
                onSuccess()
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }
}