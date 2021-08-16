package com.example.foodappsyncit.models

data class Topping(
    val name: String = "",
    val type: String = "",
    val price: Double = 0.00
)

data class ToppingWrapper(
    val topping: Topping,
    val isChecked: Boolean = false
)