package com.example.farmsmart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.farmsmart.Authentication.SignIn
import com.example.farmsmart.FirebaseService.FirebaseService

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseService.initialize(this)

        val btn = findViewById<Button>(R.id.btn)
        btn.setOnClickListener{
            val i = Intent(this,SignIn::class.java)
            startActivity(i)
            finish()
        }
    }
}