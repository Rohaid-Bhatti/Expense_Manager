package com.hzm.expensemanager.utils

import java.util.regex.Pattern

class ValidationUtils {

    companion object {
        // Function to validate email using regex
        fun isValidEmail(email: String): Boolean {
            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
            val pattern = Pattern.compile(emailPattern)
            val matcher = pattern.matcher(email)
            return matcher.matches()
        }

        // Function to validate password with at least 1 number and 1 special character
        fun isValidPassword(password: String): Boolean {
            val digitPattern = ".*\\d.*"
            val specialCharPattern = ".*[!@#\$%^&*()].*"
            return password.length >= 6 && password.matches(Regex(digitPattern)) && password.matches(
                Regex(specialCharPattern)
            )
        }

        // Function to validate phone number
        fun isValidPhoneNumber(phoneNumber: String): Boolean {
            val phoneNumberPattern = "^03\\d{9}$"
            return phoneNumber.matches(Regex(phoneNumberPattern))
        }
    }
}