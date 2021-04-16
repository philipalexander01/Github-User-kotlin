package com.example.consumer_app.ui
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.consumer_app.R
import com.example.consumer_app.ui.favorite.FavoriteActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashActivity, FavoriteActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}