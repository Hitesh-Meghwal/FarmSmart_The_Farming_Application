package com.example.farmsmart.Activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import com.example.farmsmart.R
import com.example.farmsmart.databinding.ActivitySignInBinding

class SignIn : AppCompatActivity() {
    lateinit var binding: ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val signUp = findViewById<TextView>(R.id.registerSignup)
        signUp.setOnClickListener {
            val i = Intent(this,SignUp::class.java)
            startActivity(i)
        }
        binding.forgetPassword.setOnClickListener {
            forgetPasswordDialog()
        }
    }

    private fun forgetPasswordDialog(){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_forgetpassword)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }
}