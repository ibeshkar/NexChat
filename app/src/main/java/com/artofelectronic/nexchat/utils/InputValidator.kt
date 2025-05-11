package com.artofelectronic.nexchat.utils

object InputValidator {

    /**
     * Validates the provided email address.
     */
    fun validateEmail(email: String): String? {
        val regex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
        return if (email.isBlank()) "Email is required"
        else if (!regex.matches(email)) "Invalid email format"
        else null
    }

    /**
     * Validates the provided password.
     */
    fun validatePassword(password: String): String? {
        val regex = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{6,}$")
        return if (password.isBlank()) "Password is required"
        else if (!regex.matches(password)) "Weak password (min 6 chars, 1 upper, 1 digit, 1 special)"
        else null
    }
}