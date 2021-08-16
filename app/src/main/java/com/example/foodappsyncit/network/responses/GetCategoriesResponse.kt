package com.example.foodappsyncit.network.responses

import com.example.foodappsyncit.models.Category

data class GetCategoriesResponse(
    val categories: List<Category> = emptyList()
)
