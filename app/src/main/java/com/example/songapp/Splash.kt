package com.example.songapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth

class Splash : AppCompatActivity() {
    private val SPLASH_TIME_OUT:Long = 1000
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)
        Handler().postDelayed({
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this,Login::class.java))
                finish()
            }
        }, SPLASH_TIME_OUT)
    }


}
