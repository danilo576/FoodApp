package com.example.foodappsyncit.network

import com.example.foodappsyncit.models.User
import com.example.foodappsyncit.network.requests.LoginRequest
import com.example.foodappsyncit.network.requests.UserUpdate
import com.example.foodappsyncit.network.responses.GetCategoriesResponse
import com.example.foodappsyncit.network.responses.LoginResponse
import com.example.foodappsyncit.network.responses.LogoutResponse
import com.example.foodappsyncit.network.responses.UserRegistryResponse
import retrofit2.Response

class ApiClient(
    private val foodAppService: FoodAppService
) {
    suspend fun getCategories(): Response<GetCategoriesResponse> = foodAppService.getCategories()

    suspend fun addUser(user: User): Response<UserRegistryResponse> = foodAppService.addUser(user)

    suspend fun loginUser(user: LoginRequest): Response<LoginResponse> =
        foodAppService.loginUser(user)

    suspend fun getLoggedInUser(token: String): Response<UserRegistryResponse> =
        foodAppService.getLoggedInUser(token)

    suspend fun logoutUser(token: String): Response<LogoutResponse> =
        foodAppService.logoutUser(token)

    suspend fun updateUser(
        token: String,
        user: UserUpdate
    ): Response<LoginResponse> =
        foodAppService.updateUser(token, user)
}
