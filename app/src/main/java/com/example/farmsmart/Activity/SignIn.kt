package com.example.farmsmart.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.farmsmart.R

class SignIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val signUp = findViewById<TextView>(R.id.registerSignup)
        signUp.setOnClickListener {
            val i = Intent(this,SignUp::class.java)
            startActivity(i)
        }
    }
}