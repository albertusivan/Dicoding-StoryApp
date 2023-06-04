package com.example.sub1intermediate.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.sub1intermediate.data.api.ApiConfig
import com.example.sub1intermediate.data.local.AuthDataStore
import com.example.sub1intermediate.data.repository.Repository

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("preferences")
object Injection {
    fun provideRepository(context: Context): Repository {
        val apiService = ApiConfig.getApiService()
        return Repository.getInstance(
            apiService,
            AuthDataStore.getInstance(context.dataStore),
        )
    }
}