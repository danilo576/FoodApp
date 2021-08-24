package com.example.foodappsyncit.network.responses

data class UserResponse(
    val id: Int = 0,
    var firstName: String = "",
    var lastName: String = "",
    val email: String = "",
    val password: String = "",
    val status: String? = "",
    val phone: PhoneResponse = PhoneResponse()
)