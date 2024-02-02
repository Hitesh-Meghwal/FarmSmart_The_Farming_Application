package com.example.farmsmart.Authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.farmsmart.MainScreen.MainScreen
import com.example.farmsmart.databinding.ActivitySignUpBinding

class SignUp : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.alreadyRegister.setOnClickListener {
            val goToSignin = Intent(this,SignIn::class.java)
            startActivity(goToSignin)
        }

        binding.signupBtn.setOnClickListener{
            val mainScreen = Intent(this,MainScreen::class.java)
            startActivity(mainScreen)
        }
    }
}