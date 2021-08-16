package com.example.foodappsyncit.utils

import android.content.Context

object UserPreferences {

    private fun saveToSharedPreferences(context: Context, key: String, value: String?) {
        val sharedPref = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.apply {
            putString(key, value)
            apply()
        }
    }

    private fun getFromSharedPreferences(context: Context, key: String): String? =
        context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE).getString(key, null)

    fun saveToken(context: Context, key: String, value: String?) {
        saveToSharedPreferences(context, key, value)
    }

    fun retrieveToken(context: Context, key: String): String? {
        return getFromSharedPreferences(context, key)
    }

}