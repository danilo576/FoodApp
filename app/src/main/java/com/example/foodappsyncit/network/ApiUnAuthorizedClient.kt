package com.example.foodappsyncit.network

import com.example.foodappsyncit.models.User
import com.example.foodappsyncit.network.requests.LoginRequest
import com.example.foodappsyncit.network.responses.*
import retrofit2.Response

class ApiUnAuthorizedClient(
    private val unAuthFoodAppService: UnAuthFoodAppService
) {
    suspend fun getCategories(): Response<GetCategoriesResponse> = unAuthFoodAppService.getCategories()

    suspend fun addUser(user: User): Response<UserRegistryResponse> = unAuthFoodAppService.addUser(user)

    suspend fun loginUser(user: LoginRequest): Response<LoginResponse> =
        unAuthFoodAppService.loginUser(user)
}
