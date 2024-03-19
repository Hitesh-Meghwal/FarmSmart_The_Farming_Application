package com.example.farmsmart.FieldValidation

import android.util.Patterns
class Validator {

    fun isvalidEmail(email : String):Boolean{
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isvalidPhoneN(phoneN : String):Boolean{
        val phoneRegex = "^[+]?[0-9]{10,12}\$"
        return phoneN.matches(phoneRegex.toRegex())
    }

    fun isvalidPassword(password : String):Boolean{
        val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\$%&*?])[A-Za-z\\d@\$!%*?&]{8,}\$"
        return password.matches(passwordRegex.toRegex())
    }

}