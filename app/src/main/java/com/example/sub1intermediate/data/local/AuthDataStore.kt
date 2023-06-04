package com.example.sub1intermediate.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthDataStore private constructor(private val dataStore: DataStore<Preferences>) {
    fun getToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
    }

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun clearToken() {
        dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("token")
        @Volatile
        private var INSTANCE: AuthDataStore? = null
        fun getInstance(dataStore: DataStore<Preferences>): AuthDataStore {
            return INSTANCE ?: synchronized(this){
                val instance = AuthDataStore(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}