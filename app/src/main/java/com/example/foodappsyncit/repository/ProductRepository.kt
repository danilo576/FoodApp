package com.example.foodappsyncit.repository

import com.example.foodappsyncit.network.NetworkLayer
import com.example.foodappsyncit.network.responses.FavoriteProductsResponse
import com.example.foodappsyncit.network.responses.MessageResponse
import retrofit2.Response

class ProductRepository {

    suspend fun readAllFavorites(token: String): Response<FavoriteProductsResponse> =
        NetworkLayer.apiClient.readAllFavorites(token)

    suspend fun addProductToFavorites(
        token: String,
        productId: Int
    ): Response<MessageResponse> =
        NetworkLayer.apiClient.addProductToFavorites(token, productId)

    suspend fun deleteProductFromFavorites(
        token: String,
        productId: Int
    ): Response<MessageResponse> =
        NetworkLayer.apiClient.deleteProductFromFavorites(token, productId)
}