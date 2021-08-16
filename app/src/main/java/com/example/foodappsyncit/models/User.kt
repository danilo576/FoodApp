package com.example.foodappsyncit.models

data class User(
    val appVersion: String = "",
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val os: String = "",
    val password: String = "",
    val password_confirmation: String = "",
    val phoneModel: String = "",
    val phoneNumber: String = "",
    val status: String = ""
)