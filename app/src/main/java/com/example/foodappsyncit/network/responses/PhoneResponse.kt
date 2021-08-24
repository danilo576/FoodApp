package com.example.foodappsyncit.network.responses

data class PhoneResponse(
    val id: Int = 0,
    val os: String = "",
    var phoneNumber: String = "",
    val phoneModel: String = "",
    val appVersion: String = ""
)