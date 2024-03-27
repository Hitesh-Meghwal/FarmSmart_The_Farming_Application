package com.example.farmsmart.Authentication

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import com.example.farmsmart.Activity.MainScreen
import com.example.farmsmart.Dialog.LoadingUtils
import com.example.farmsmart.FieldValidation.Validator
import com.example.farmsmart.FirebaseService.FirebaseService
import com.example.farmsmart.Fragments.Home
import com.example.farmsmart.R
import com.example.farmsmart.databinding.ActivitySignUpBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class SignUp : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    private val RC_SIGN_IN = 1;
    private val TAG = "GOOGLEAUTH";
    lateinit var auth: FirebaseAuth
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
        binding.googlesigninBtn.setOnClickListener{
            googleSignIn()
        }
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

        val auth = FirebaseService.getAuthInstance()
        val firestore = FirebaseService.getFirestoreInstance()
        auth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                firestore.collection("UserDetails")
                    .document(auth.uid.toString())
                    .set(UserModel(name,phoneNo,email,password))
                    .addOnCompleteListener {task->
                        LoadingUtils.hideDialog()
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
                LoadingUtils.hideDialog()
                notifyUser(it.message.toString())
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
            catch (e: ApiException){
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

    private fun notifyUser(msg : String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}