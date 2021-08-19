package com.example.foodappsyncit.network

import com.example.foodappsyncit.network.requests.UserUpdate
import com.example.foodappsyncit.network.responses.*
import retrofit2.Response

class ApiAuthorizedClient(private val authFoodAppService: AuthFoodAppService) {
    suspend fun getLoggedInUser(): Response<UserRegistryResponse> =
        authFoodAppService.getLoggedInUser()

    suspend fun logoutUser(): Response<MessageResponse> =
        authFoodAppService.logoutUser()

    suspend fun updateUser(
        user: UserUpdate
    ): Response<LoginResponse> =
        authFoodAppService.updateUser(user)

    suspend fun readAllFavorites(): Response<FavoriteProductsResponse> =
        authFoodAppService.readAllFavorites()

    suspend fun addProductToFavorites(
        productId: Int
    ): Response<MessageResponse> =
        authFoodAppService.addProductToFavorites(productId)

    suspend fun deleteProductFromFavorites(
        productId: Int
    ): Response<MessageResponse> =
        authFoodAppService.deleteProductFromFavorites(productId)
}