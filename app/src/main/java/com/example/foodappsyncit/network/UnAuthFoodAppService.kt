package com.example.foodappsyncit.network

import com.example.foodappsyncit.models.User
import com.example.foodappsyncit.network.requests.LoginRequest
import com.example.foodappsyncit.network.responses.*
import com.example.foodappsyncit.utils.Constants.Companion.CATEGORIES_URL
import com.example.foodappsyncit.utils.Constants.Companion.LOGIN_URL
import com.example.foodappsyncit.utils.Constants.Companion.REGISTER_URL
import retrofit2.Response
import retrofit2.http.*

interface UnAuthFoodAppService {
    @GET(CATEGORIES_URL)
    suspend fun getCategories(): Response<GetCategoriesResponse>

    @POST(REGISTER_URL)
    suspend fun addUser(@Body userData: User): Response<UserRegistryResponse>

    @POST(LOGIN_URL)
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>

}