package com.hzm.expensemanager

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity() {
    private val SPLASH_DELAY: Long = 2000 // 2 seconds
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_splash)

        sharedPreferences = getSharedPreferences("MY_PRE", Context.MODE_PRIVATE)

        // Delay for a few seconds and then launch the main activity
        Handler().postDelayed({
            /*val intent = Intent(this@SplashActivity, DashboardActivity::class.java)
            startActivity(intent)*/
            decideNextActivity()
        }, SPLASH_DELAY)
    }

    private fun decideNextActivity() {
        val isLoggedIn = sharedPreferences.getBoolean("ISCHECK", false)

        val intent = if (isLoggedIn) {
            Intent(this@SplashActivity, MainActivity::class.java)
        } else {
            Intent(this@SplashActivity, LoginActivity::class.java)
        }

        startActivity(intent)
        finish()
    }
}