package com.example.foodappsyncit.network

import com.example.foodappsyncit.network.requests.UserUpdate
import com.example.foodappsyncit.network.responses.FavoriteProductsResponse
import com.example.foodappsyncit.network.responses.LoginResponse
import com.example.foodappsyncit.network.responses.MessageResponse
import com.example.foodappsyncit.network.responses.UserRegistryResponse
import com.example.foodappsyncit.utils.Constants
import retrofit2.Response
import retrofit2.http.*

interface AuthFoodAppService {

    @GET(Constants.LOGGED_URL)
    suspend fun getLoggedInUser(): Response<UserRegistryResponse>

    @POST(Constants.LOGOUT_URL)
    suspend fun logoutUser(): Response<MessageResponse>

    @PUT(Constants.UPDATE_USER_URL)
    suspend fun updateUser(
        @Body user: UserUpdate
    ): Response<LoginResponse>

    @GET(Constants.ALL_FAVORITES_URL)
    suspend fun readAllFavorites(): Response<FavoriteProductsResponse>

    @POST(Constants.ADD_FAVORITE_URL)
    suspend fun addProductToFavorites(
        @Path("productId") productId: Int
    ): Response<MessageResponse>

    @DELETE(Constants.DELETE_FAVORITE_PRODUCT_URL)
    suspend fun deleteProductFromFavorites(
        @Path("productId") productId: Int
    ): Response<MessageResponse>
}