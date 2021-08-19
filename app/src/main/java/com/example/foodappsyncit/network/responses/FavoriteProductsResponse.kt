package com.example.foodappsyncit.network.responses

import com.example.foodappsyncit.models.Product

data class FavoriteProductsResponse(
    val favoriteProducts: List<Product> = listOf()
)
