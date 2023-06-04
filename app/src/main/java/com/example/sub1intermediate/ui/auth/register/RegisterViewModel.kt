package com.example.sub1intermediate.ui.auth.register

import androidx.lifecycle.ViewModel
import com.example.sub1intermediate.data.repository.Repository

class RegisterViewModel(private val repository: Repository) : ViewModel() {
    fun saveUserRegister(name: String, email: String, password: String) =
        repository.saveUserRegister(name, email, password)
}