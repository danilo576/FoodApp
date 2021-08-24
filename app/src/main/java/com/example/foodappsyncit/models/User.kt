package com.example.foodappsyncit.models

data class User(
    val appVersion: String = "",
    val email: String = "",
    var firstName: String = "",
    var lastName: String = "",
    val os: String = "",
    val password: String = "",
    val password_confirmation: String = "",
    val phoneModel: String = "",
    var phoneNumber: String = "",
    val status: String = ""
)