package com.example.foodappsyncit.network.responses

data class UserResponse(
    val id: Int = 0,
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val status: String? = "",
    val phone: PhoneResponse = PhoneResponse()
)