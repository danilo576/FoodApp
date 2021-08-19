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
        val request = NetworkLayer.apiUnAuthorizedClient.getCategories()

        if (request.isSuccessful) {
            return request.body()!!
        }
        return null
    }

    suspend fun addUser(user: User): Response<UserRegistryResponse> =
        NetworkLayer.apiUnAuthorizedClient.addUser(user)

    suspend fun loginUser(user: LoginRequest): Response<LoginResponse> =
        NetworkLayer.apiUnAuthorizedClient.loginUser(user)

    suspend fun getLoggedInUser(): Response<UserRegistryResponse> =
        NetworkLayer.apiAuthorizedClient.getLoggedInUser()

    suspend fun logoutUser(): Response<MessageResponse> =
        NetworkLayer.apiAuthorizedClient.logoutUser()

    suspend fun updateUser(
        user: UserUpdate
    ): Response<LoginResponse> =
        NetworkLayer.apiAuthorizedClient.updateUser(user)
}