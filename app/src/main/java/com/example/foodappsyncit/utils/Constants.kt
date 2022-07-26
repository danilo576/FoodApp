package com.example.foodappsyncit.utils

class Constants {

    companion object {
        const val BASE_URL = "https://food-app.syncitgroup.dev"
        const val CATEGORIES_URL = "/api/categories"
        const val REGISTER_URL = "/api/register"
        const val LOGIN_URL = "/api/login"
        const val LOGGED_URL = "/api/user"
        const val LOGOUT_URL = "/api/logout"
        const val UPDATE_USER_URL = "/api/user/update"
        const val PREFS_NAME = "myPrefs"
        const val ADD_FAVORITE_URL = "/api/addToFavorites/{productId}"
        const val ALL_FAVORITES_URL = "/api/favoriteProducts"
        const val DELETE_FAVORITE_PRODUCT_URL = "/api/removeFromFavorites/{productId}"
    }
}