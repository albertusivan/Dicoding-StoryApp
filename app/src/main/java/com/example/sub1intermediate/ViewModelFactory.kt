package com.example.sub1intermediate

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sub1intermediate.ui.upload.UploadViewModel
import com.example.sub1intermediate.ui.auth.login.LoginViewModel
import com.example.sub1intermediate.ui.auth.register.RegisterViewModel
import com.example.sub1intermediate.ui.detail.DetailViewModel
import com.example.sub1intermediate.data.di.Injection
import com.example.sub1intermediate.ui.main.MainViewModel
import com.example.sub1intermediate.data.repository.Repository
import com.example.sub1intermediate.ui.map.MapsViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory private constructor(private val repository: Repository): ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> return LoginViewModel(repository) as T
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> return RegisterViewModel(repository) as T
            modelClass.isAssignableFrom(MainViewModel::class.java) -> return MainViewModel(repository) as T
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> return DetailViewModel(repository) as T
            modelClass.isAssignableFrom(UploadViewModel::class.java) -> return UploadViewModel(repository) as T
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> return MapsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel : " + modelClass.name)
    }

    companion object{
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory = instance?: synchronized(this) {
            instance ?: ViewModelFactory(Injection.provideRepository(context))
        }.also { instance = it }
    }
}