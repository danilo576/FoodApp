package com.example.foodappsyncit.activities

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.foodappsyncit.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var badgeCounter: Int = 0

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        bottomNav.setupWithNavController(navController)

        if (intent.getStringExtra("Guest") != null) {
            bottomNav.menu.findItem(R.id.profileFragment).isVisible = false
        }
    }

    internal fun incrementBadge(sign: String) {
        when (sign) {
            "+" -> badgeCounter++
            "-" -> badgeCounter--
            "empty" -> badgeCounter = 0
        }

        bottomNav.apply {
            getOrCreateBadge(R.id.cartFragment).isVisible = true
            getOrCreateBadge(R.id.cartFragment).backgroundColor = Color.parseColor("#1FA5E1")
            getOrCreateBadge(R.id.cartFragment).badgeTextColor = Color.parseColor("#FFFFFF")
            if (badgeCounter == 0) {
                getOrCreateBadge(R.id.cartFragment).isVisible = false
            } else {
                getOrCreateBadge(R.id.cartFragment).number = badgeCounter
            }
        }
    }

    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}