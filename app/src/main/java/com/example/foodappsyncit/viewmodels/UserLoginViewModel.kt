package com.example.foodappsyncit.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodappsyncit.network.requests.LoginRequest
import com.example.foodappsyncit.network.requests.UserUpdate
import com.example.foodappsyncit.network.responses.LoginResponse
import com.example.foodappsyncit.network.responses.MessageResponse
import com.example.foodappsyncit.network.responses.UserRegistryResponse
import com.example.foodappsyncit.repository.SharedRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class UserLoginViewModel() : ViewModel() {

    private val repository = SharedRepository()
    var loginResponse: MutableLiveData<Response<LoginResponse>> = MutableLiveData()
    var loggedInUser: MutableLiveData<Response<UserRegistryResponse>> = MutableLiveData()
    var logoutUser: MutableLiveData<Response<MessageResponse>> = MutableLiveData()
    var updatedUser: MutableLiveData<Response<LoginResponse>> = MutableLiveData()

    fun loginUser(user: LoginRequest) {
        viewModelScope.launch {
            val response = repository.loginUser(user)
            loginResponse.value = response
        }
    }

    fun getLoggedInUser(token: String) {
        viewModelScope.launch {
            val response = repository.getLoggedInUser(token)
            loggedInUser.value = response
        }
    }

    fun logoutUser(token: String) {
        viewModelScope.launch {
            val response = repository.logoutUser(token)
            logoutUser.value = response
        }
    }

    fun updateUser(token: String, user: UserUpdate) {
        viewModelScope.launch {
            val response = repository.updateUser(token, user)
            updatedUser.value = response
        }
    }
}