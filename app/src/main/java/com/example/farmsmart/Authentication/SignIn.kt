package com.example.farmsmart.Authentication

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.farmsmart.Activity.MainScreen
import com.example.farmsmart.FieldValidation.Validator
import com.example.farmsmart.FirebaseService.FirebaseService
import com.example.farmsmart.Fragments.Home
import com.example.farmsmart.R
import com.example.farmsmart.databinding.ActivitySignInBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class SignIn : AppCompatActivity() {
    lateinit var binding: ActivitySignInBinding
    lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = ProgressDialog(this)
        val signUp = findViewById<TextView>(R.id.registerSignup)
        signUp.setOnClickListener {
            val i = Intent(this,SignUp::class.java)
            startActivity(i)
        }
        binding.forgetPassword.setOnClickListener {
            forgetPasswordDialog()
        }
        binding.signinBtn.setOnClickListener{
            signIn()
        }
    }

    private fun signIn(){
        val email = binding.signinEmail.text.toString()
        val password = binding.signinPassword.text.toString()

        if (email.isEmpty()){
            binding.signinEmail.error = "Email is required."
            return
        }
        else if (!Validator().isvalidEmail(email)){
            binding.signinEmail.error = "Invalid Email, please check again."
            return
        }

        if (password.isEmpty()){
            binding.signinPassword.error = "Password is required."
            return
        }
        else if (password.length < 6){
            binding.signinPassword.error = "Password must be at least 8 characters long."
            return
        }
        else if (!Validator().isvalidPassword(password)){
            binding.signinPassword.error = "Password must contain at least one Uppercase letter, one Lowercase letter, one digit and one Special character."
            return
        }
        else {
            binding.signinPassword.error = null
        }

        val auth = FirebaseService.getAuthInstance()
        auth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                val goToHome = Intent(this,MainScreen::class.java)
                startActivity(goToHome)
                finish()
                notifyUser("Signing Successfully")
                progressDialog.cancel()
            }
            .addOnFailureListener {
                notifyUser(it.message.toString())
                progressDialog.cancel()
            }

        progressDialog.setMessage("Signing...")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()

    }

    private fun forgetPasswordDialog(){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_forgetpassword)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val email = dialog.findViewById<TextInputEditText>(R.id.txt_forgetemail)
        dialog.findViewById<MaterialButton>(R.id.submit_btn).setOnClickListener {
            val forgetEmail = email.text.toString()
            if (forgetEmail.isEmpty()){
                email.error = "Email is required."
                return@setOnClickListener
            }
            else if (!Validator().isvalidEmail(forgetEmail)){
                email.error = "Invalid Email, please check again."
                return@setOnClickListener
            }
            forgetPassword(forgetEmail)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun forgetPassword(email : String){
        val auth = FirebaseService.getAuthInstance()
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                notifyUser("Email has been sent to your inbox.")
            }
            .addOnFailureListener { e->
                notifyUser(e.message.toString())
            }

    }

    override fun onStart() {
        val currentUser = FirebaseService.getAuthInstance().currentUser
        if (currentUser != null){
            val stayInHome = Intent(this,MainScreen::class.java)
            startActivity(stayInHome)
        }
        super.onStart()

    }

    private fun notifyUser(msg:String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}