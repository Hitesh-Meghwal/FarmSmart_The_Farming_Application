package com.example.farmsmart.Authentication

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.farmsmart.Activity.MainScreen
import com.example.farmsmart.Dialog.LoadingUtils
import com.example.farmsmart.FieldValidation.Validator
import com.example.farmsmart.FirebaseService.FirebaseService
import com.example.farmsmart.Fragments.Home
import com.example.farmsmart.R
import com.example.farmsmart.databinding.ActivitySignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class SignIn : AppCompatActivity() {
    lateinit var binding: ActivitySignInBinding
    private val RC_SIGN_IN = 1;
    private val TAG = "GOOGLEAUTH";
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseService.getAuthInstance()
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
        binding.googlesigninBtn.setOnClickListener{
            googleSignIn()
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

        auth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                val goToHome = Intent(this,MainScreen::class.java)
                startActivity(goToHome)
                finish()
                notifyUser("Signing Successfully")
                LoadingUtils.hideDialog()
            }
            .addOnFailureListener {
                notifyUser(it.message.toString())
                LoadingUtils.hideDialog()

            }

        LoadingUtils.showDialog(this,true)

    }

    private fun googleSignIn(){

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val  mGoogleSignInClient = GoogleSignIn.getClient(this,gso)

        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent,RC_SIGN_IN)

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                handleSignResult(account)
            }
            catch (e:ApiException){
                Log.d(TAG,"Google Sign in Failed $e")
            }
        }
    }

    private fun handleSignResult(account: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(account.idToken,null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task->
                if(task.isSuccessful){
                    val user = auth.currentUser
                    if (user != null) {
                        addUsertoFirebase(user)
                    }
                }
                else{
                    Log.d(TAG,"SignInCredential:Failure",task.exception)
                }
            }
    }

    private fun addUsertoFirebase(user: FirebaseUser) {
        val Usermap = hashMapOf(
            "Name" to user.displayName,
            "Email" to user.email,
            "PhotoUrl" to user.photoUrl
        )
        val firestore = FirebaseService.getFirestoreInstance()
        firestore.collection("GoogleSign UserDetails")
            .document(user.uid)
            .set(Usermap)
            .addOnSuccessListener {
                val goToHome = Intent(this,MainScreen::class.java)
                startActivity(goToHome)
                finish()
                notifyUser("Signing Successfully")
                LoadingUtils.hideDialog()
            }
            .addOnFailureListener {
                notifyUser(it.message.toString())
                LoadingUtils.hideDialog()
            }
        LoadingUtils.showDialog(this,true)

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