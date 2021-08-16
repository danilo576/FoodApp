package com.example.foodappsyncit.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.foodappsyncit.R
import com.example.foodappsyncit.controllers.CartController
import com.example.foodappsyncit.utils.UserPreferences

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({

            if (UserPreferences.retrieveToken(this@SplashActivity, "token") != null) {
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
    }
}