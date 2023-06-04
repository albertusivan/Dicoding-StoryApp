package com.example.sub1intermediate.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.sub1intermediate.data.repository.Repository
import kotlinx.coroutines.Dispatchers

class MapsViewModel(private val repository: Repository): ViewModel()  {
    fun getToken() = repository.getToken().asLiveData(Dispatchers.IO)

    fun getLocation(token : String) = repository.getLocation(token)
}