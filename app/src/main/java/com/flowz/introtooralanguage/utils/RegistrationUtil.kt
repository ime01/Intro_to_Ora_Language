package com.flowz.introtooralanguage.utils

object RegistrationUtil {

    private val currentUsers = listOf("Mark", "Klause", "Fredrick", "Greg")

    /**
     * the input is not valid if...
     * ...the username/password is empty
     * ...the username is already in use
     * ...confirmed password is not the same as the real password
     * ...password contains less than 2 digits
     */

    fun validateRegistrationInput(username: String, password:String, confirmedPassword:String): Boolean{

        if (username.isEmpty() || password.isEmpty()){
            return false
        }

        if (username in currentUsers){
            return false
        }

        if (password!= confirmedPassword){
            return false
        }

        if (password.count { it.isDigit() }<2){
            return false
        }

        return true
    }


}