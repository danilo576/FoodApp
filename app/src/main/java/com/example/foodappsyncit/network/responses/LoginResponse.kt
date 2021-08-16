package com.example.foodappsyncit.network.responses

data class LoginResponse(
    val user: UserResponse = UserResponse(),
    val token: String = ""
)