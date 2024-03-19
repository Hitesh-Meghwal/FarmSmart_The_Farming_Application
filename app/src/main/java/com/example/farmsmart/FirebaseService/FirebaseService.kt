package com.example.farmsmart.FirebaseService

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseService {

    fun initialize(context : Context){
        FirebaseApp.initializeApp(context)
    }

    fun getAuthInstance() : FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    fun getFirestoreInstance() : FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }
}