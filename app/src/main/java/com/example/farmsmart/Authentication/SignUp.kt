package com.example.farmsmart.Authentication

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import com.example.farmsmart.Activity.MainScreen
import com.example.farmsmart.FieldValidation.Validator
import com.example.farmsmart.FirebaseService.FirebaseService
import com.example.farmsmart.Fragments.Home
import com.example.farmsmart.databinding.ActivitySignUpBinding

class SignUp : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.alreadyRegister.setOnClickListener {
            val goToSignin = Intent(this,SignIn::class.java)
            startActivity(goToSignin)
        }

        binding.signupBtn.setOnClickListener{
            signUp()
        }
        progressDialog = ProgressDialog(this)
    }

    private fun signUp(){
        val name = binding.userName.text.toString()
        val phoneNo = binding.phoneNumber.text.toString()
        val email = binding.signupEmail.text.toString()
        val password = binding.signupPassword.text.toString()

        if (name.isEmpty()){
            binding.userName.error = "Name is required."
            return
        }

        if (phoneNo.isEmpty()){
            binding.phoneNumber.error = "Phone number is required."
            return
        }
        else if (!Validator().isvalidPhoneN(phoneNo)) {
            binding.phoneNumber.error = "Invalid phone number."
            return
        }

        if (email.isEmpty()){
            binding.signupEmail.error = "Email is required."
            return
        }
        else if (!Validator().isvalidEmail(email)){
            binding.signupEmail.error = "Invalid Email, please check again."
            return
        }

        if (password.isEmpty()){
            binding.signupPassword.error = "Password is required."
            return
        }
        else if (password.length < 6){
            binding.signupPassword.error = "Password must be at least 8 characters long."
            return
        }
        else if (!Validator().isvalidPassword(password)){
            binding.signupPassword.error = "Password must contain at least one Uppercase letter, one Lowercase letter, one digit and one Special character."
            return
        }
        else {
            binding.signupPassword.error = null
        }

        progressDialog.setMessage("Signing...")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()

        val auth = FirebaseService.getAuthInstance()
        val firestore = FirebaseService.getFirestoreInstance()
        auth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                firestore.collection("UserDetails")
                    .document(auth.uid.toString())
                    .set(UserModel(name,phoneNo,email,password))
                    .addOnCompleteListener {task->
                        progressDialog.dismiss()
                        if (task.isSuccessful){
                            val goToHome = Intent(this,MainScreen::class.java)
                            startActivity(goToHome)
                            finish()
                            notifyUser("Registered Successfully.")
                        }
                        else{
                            notifyUser("Registration failed.")
                        }
                    }
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                notifyUser(it.message.toString())
            }

    }

    private fun notifyUser(msg : String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}