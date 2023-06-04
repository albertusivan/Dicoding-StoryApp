package com.example.sub1intermediate.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.sub1intermediate.data.repository.Repository
import kotlinx.coroutines.Dispatchers

class LoginViewModel(private val repository: Repository) : ViewModel() {
    fun getUserLogin(email: String, password: String) =
        repository.getUserLogin(email, password)

    fun getToken() = repository.getToken().asLiveData(Dispatchers.IO)
}