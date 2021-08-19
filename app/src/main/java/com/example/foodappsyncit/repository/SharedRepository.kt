package com.example.foodappsyncit.repository

import com.example.foodappsyncit.models.User
import com.example.foodappsyncit.network.NetworkLayer
import com.example.foodappsyncit.network.requests.LoginRequest
import com.example.foodappsyncit.network.requests.UserUpdate
import com.example.foodappsyncit.network.responses.GetCategoriesResponse
import com.example.foodappsyncit.network.responses.LoginResponse
import com.example.foodappsyncit.network.responses.MessageResponse
import com.example.foodappsyncit.network.responses.UserRegistryResponse
import retrofit2.Response

class SharedRepository {

    suspend fun getCategories(): GetCategoriesResponse? {
        val request = NetworkLayer.apiClient.getCategories()

        if (request.isSuccessful) {
            return request.body()!!
        }
        return null
    }

    suspend fun addUser(user: User): Response<UserRegistryResponse> =
        NetworkLayer.apiClient.addUser(user)

    suspend fun loginUser(user: LoginRequest): Response<LoginResponse> =
        NetworkLayer.apiClient.loginUser(user)

    suspend fun getLoggedInUser(token: String): Response<UserRegistryResponse> =
        NetworkLayer.apiClient.getLoggedInUser(token)

    suspend fun logoutUser(token: String): Response<MessageResponse> =
        NetworkLayer.apiClient.logoutUser(token)

    suspend fun updateUser(
        token: String,
        user: UserUpdate
    ): Response<LoginResponse> =
        NetworkLayer.apiClient.updateUser(token, user)
}