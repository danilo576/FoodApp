package com.example.foodappsyncit.repository

import com.example.foodappsyncit.network.NetworkLayer
import com.example.foodappsyncit.network.responses.FavoriteProductsResponse
import com.example.foodappsyncit.network.responses.MessageResponse
import retrofit2.Response

class ProductRepository {

    suspend fun readAllFavorites(): Response<FavoriteProductsResponse> =
        NetworkLayer.apiAuthorizedClient.readAllFavorites()

    suspend fun addProductToFavorites(
        productId: Int
    ): Response<MessageResponse> =
        NetworkLayer.apiAuthorizedClient.addProductToFavorites(productId)

    suspend fun deleteProductFromFavorites(
        productId: Int
    ): Response<MessageResponse> =
        NetworkLayer.apiAuthorizedClient.deleteProductFromFavorites(productId)
}