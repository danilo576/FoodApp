package com.example.foodappsyncit.network

import com.example.foodappsyncit.models.User
import com.example.foodappsyncit.network.requests.LoginRequest
import com.example.foodappsyncit.network.requests.UserUpdate
import com.example.foodappsyncit.network.responses.*
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

    suspend fun logoutUser(token: String): Response<MessageResponse> =
        foodAppService.logoutUser(token)

    suspend fun updateUser(
        token: String,
        user: UserUpdate
    ): Response<LoginResponse> =
        foodAppService.updateUser(token, user)

    suspend fun readAllFavorites(token: String): Response<FavoriteProductsResponse> =
        foodAppService.readAllFavorites(token)

    suspend fun addProductToFavorites(
        token: String,
        productId: Int
    ): Response<MessageResponse> =
        foodAppService.addProductToFavorites(token, productId)

    suspend fun deleteProductFromFavorites(
        token: String,
        productId: Int
    ): Response<MessageResponse> =
        foodAppService.deleteProductFromFavorites(token, productId)
}
