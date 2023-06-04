package com.example.sub1intermediate.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.sub1intermediate.data.model.ListStoryItem
import com.example.sub1intermediate.data.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository): ViewModel()  {
    fun getToken() = repository.getToken().asLiveData(Dispatchers.IO)

    fun getAllStory(token: String) : LiveData<PagingData<ListStoryItem>> {
        return repository.getAllStory(token).cachedIn(viewModelScope) }

    fun clearToken() {
        viewModelScope.launch {
            repository.clearToken()
        }
    }
}