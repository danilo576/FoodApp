package com.example.foodappsyncit.network

import com.example.foodappsyncit.models.User
import com.example.foodappsyncit.network.requests.LoginRequest
import com.example.foodappsyncit.network.requests.UserUpdate
import com.example.foodappsyncit.network.responses.GetCategoriesResponse
import com.example.foodappsyncit.network.responses.LoginResponse
import com.example.foodappsyncit.network.responses.LogoutResponse
import com.example.foodappsyncit.network.responses.UserRegistryResponse
import com.example.foodappsyncit.utils.Constants.Companion.CATEGORIES_URL
import com.example.foodappsyncit.utils.Constants.Companion.LOGGED_URL
import com.example.foodappsyncit.utils.Constants.Companion.LOGIN_URL
import com.example.foodappsyncit.utils.Constants.Companion.LOGOUT_URL
import com.example.foodappsyncit.utils.Constants.Companion.REGISTER_URL
import com.example.foodappsyncit.utils.Constants.Companion.UPDATE_USER_URL
import retrofit2.Response
import retrofit2.http.*

interface FoodAppService {
    @GET(CATEGORIES_URL)
    suspend fun getCategories(): Response<GetCategoriesResponse>

    @Headers(
        "Content-Type:application/json",
        "Accept:application/json"
    )
    @POST(REGISTER_URL)
    suspend fun addUser(@Body userData: User): Response<UserRegistryResponse>

    @Headers(
        "Content-Type:application/json",
        "Accept:application/json"
    )
    @POST(LOGIN_URL)
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>

    @Headers("Accept:application/json")
    @GET(LOGGED_URL)
    suspend fun getLoggedInUser(@Header("Authorization") token: String): Response<UserRegistryResponse>

    @Headers("Accept:application/json")
    @POST(LOGOUT_URL)
    suspend fun logoutUser(@Header("Authorization") token: String): Response<LogoutResponse>

    @Headers(
        "Accept:application/json",
        "Content-Type:application/json"
    )
    @PUT(UPDATE_USER_URL)
    suspend fun updateUser(
        @Header("Authorization") token: String,
        @Body user: UserUpdate
    ): Response<LoginResponse>

}