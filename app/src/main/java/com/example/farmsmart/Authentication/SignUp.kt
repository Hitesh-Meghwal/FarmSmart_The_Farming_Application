package com.example.farmsmart.Authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.farmsmart.Activity.MainScreen
import com.example.farmsmart.FieldValidation.Validator
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
//            val mainScreen = Intent(this, MainScreen::class.java)
//            startActivity(mainScreen)
            signUp()
        }
    }

    private fun signUp(){
        val name = binding.userName.text.toString()
        val phoneNo = binding.phoneNumber.text.toString()
        val email = binding.signupEmail.text.toString()
        val password = binding.signupPassword.text.toString()

        if (name.isEmpty()){
            binding.userName.error = "Name is required."
        }

        if (phoneNo.isEmpty()){
            binding.phoneNumber.error = "Phone number is required."
        }
        else if (!Validator().isvalidPhoneN(phoneNo)) {
            binding.phoneNumber.error = "Invalid phone number."
        }

        if (email.isEmpty()){
            binding.signupEmail.error = "Email is required."
        }
        else if (!Validator().isvalidEmail(email)){
            binding.signupEmail.error = "Invalid Email, please check again."
        }

        if (password.isEmpty()){
            binding.signupPassword.error = "Password is required."
        }
        else if (password.length < 6){
            binding.signupPassword.error = "Password must be at least 8 characters long."
        }
        else if (!Validator().isvalidPassword(password)){
            binding.signupPassword.error = "Password must contain at least one Uppercase letter, one Lowercase letter, one digit and one Special character."
        }


    }

    private fun notifyMsg(msg : String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}