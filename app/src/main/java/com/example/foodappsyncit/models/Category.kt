package com.example.foodappsyncit.models

data class Category(
    val id: Int = 0,
    var name: String = "",
    val products: List<Product> = listOf(),
    val status: String = ""
)