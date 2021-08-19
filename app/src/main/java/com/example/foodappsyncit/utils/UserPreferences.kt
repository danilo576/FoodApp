package com.example.foodappsyncit.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.foodappsyncit.utils.Constants.Companion.PREFS_NAME

object UserPreferences {

    private var preferences: SharedPreferences? = null

    fun initPreferences(context: Context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    private fun saveToSharedPreferences(key: String, value: String?) {
        val editor = preferences?.edit()
        editor?.apply {
            putString(key, value)
            apply()
        }
    }

    private fun getFromSharedPreferences(key: String): String? =
        preferences?.getString(key, null)

    fun saveToken(key: String, value: String?) {
        saveToSharedPreferences(key, value)
    }

    fun retrieveToken(key: String): String? {
        return getFromSharedPreferences(key)
    }

}