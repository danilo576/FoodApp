package com.example.foodappsyncit.activities

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodappsyncit.R
import com.example.foodappsyncit.utils.UserPreferences
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        UserPreferences.initPreferences(this)

        if (isInternetAvailable(this)) {
            Handler(Looper.getMainLooper()).postDelayed({

                if (UserPreferences.retrieveToken("token") != null) {
                    Intent(this, MainActivity::class.java).also {
                        startActivity(it)
                        this.finish()
                    }
                } else {
                    Intent(this, MainActivity::class.java).apply {
                        putExtra("Guest", "guest")
                        startActivity(this)
                    }
                    this.finish()
                }

            }, 1000)
        } else {
            Toast.makeText(this, "Make your internet connection available!", Toast.LENGTH_LONG)
                .show()
            this.finish()
        }

    }

    private fun isInternetAvailable(context: Context): Boolean {
        val result: Boolean
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }

        return result
    }
}