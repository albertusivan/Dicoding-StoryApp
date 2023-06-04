package com.example.sub1intermediate.ui.upload

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.sub1intermediate.data.repository.Repository
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody

class UploadViewModel(private val repository: Repository) : ViewModel() {
    fun getToken() = repository.getToken().asLiveData(Dispatchers.IO)

    fun uploadStory(token: String, description: String, image: MultipartBody.Part) =
        repository.uploadStory(token, description, image)
}