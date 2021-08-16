package com.example.foodappsyncit.network.responses

data class UserRegistryResponse(
    val user: UserResponse = UserResponse(),
    val token: String = ""
)