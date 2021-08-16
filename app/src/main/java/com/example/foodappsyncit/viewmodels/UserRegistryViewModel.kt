package com.example.foodappsyncit.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodappsyncit.models.User
import com.example.foodappsyncit.network.responses.UserRegistryResponse
import com.example.foodappsyncit.repository.SharedRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class UserRegistryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SharedRepository()
    var registryResponse: MutableLiveData<Response<UserRegistryResponse>> = MutableLiveData()

    fun addUser(user: User) {
        viewModelScope.launch {
            val response = repository.addUser(user)
            registryResponse.postValue(response)
        }
    }
}