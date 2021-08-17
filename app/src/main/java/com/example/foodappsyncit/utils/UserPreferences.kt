package com.example.foodappsyncit.utils

import android.content.Context
import com.example.foodappsyncit.utils.Constants.Companion.PREFS_NAME

object UserPreferences {

    private fun saveToSharedPreferences(context: Context, key: String, value: String?) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.apply {
            putString(key, value)
            apply()
        }
    }

    private fun getFromSharedPreferences(context: Context, key: String): String? =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getString(key, null)

    fun saveToken(context: Context, key: String, value: String?) {
        saveToSharedPreferences(context, key, value)
    }

    fun retrieveToken(context: Context, key: String): String? {
        return getFromSharedPreferences(context, key)
    }

}