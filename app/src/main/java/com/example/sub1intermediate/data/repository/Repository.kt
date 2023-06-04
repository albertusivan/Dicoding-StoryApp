package com.example.sub1intermediate.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.sub1intermediate.data.api.ApiService
import com.example.sub1intermediate.data.local.AuthDataStore
import com.example.sub1intermediate.data.model.*
import com.example.sub1intermediate.data.pagging.StoryPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class Repository (
    private val apiService: ApiService,
    private val authDataStore: AuthDataStore,
    ){

    fun getUserLogin(email: String, password: String): LiveData<Result<LoginResponse>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            saveToken(response.loginResult.token)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun saveUserRegister(name: String, email: String, password: String): LiveData<Result<RegisterResponse>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = apiService.register(name, email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getToken(): Flow<String?> = authDataStore.getToken()

    private suspend fun saveToken(token: String) {
        authDataStore.saveToken(token)
    }

    suspend fun clearToken() {
        authDataStore.clearToken()
    }

    fun getAllStory(token: String): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, generateBearerToken(token))
            }
        ).liveData
    }

    private fun generateBearerToken(token: String): String {
        return if (token.contains("bearer", true)) {
            token
        } else {
            "Bearer $token"
        }
    }

    fun getDetailStory(token: String, id: String): LiveData<Result<DetailStoryResponse>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = apiService.getDetailStory(generateBearerToken(token), id)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun uploadStory(token: String, description: String, image: MultipartBody.Part): LiveData<Result<AddStoryResponse>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = apiService.uploadStory(generateBearerToken(token), description.toRequestBody("text/plain".toMediaType()), image)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getLocation(token: String):LiveData<Result<GetAllStoryResponse>> = liveData(Dispatchers.IO){
        emit(Result.Loading)
        try {
            val response = apiService.getLocation(generateBearerToken(token), location = 1)
            emit(Result.Success(response))
        }catch (e:Exception){
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            prefDataStore: AuthDataStore,
        ): Repository = instance ?: synchronized(this) {
            instance ?: Repository(apiService, prefDataStore)
        }.also { instance = it }
    }
}