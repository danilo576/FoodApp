package com.example.foodappsyncit.utils

import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import java.util.regex.Pattern

object ValidationUtil {

    fun showToast(context: Context, message: String) =
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

    private fun isNullOrEmpty(input: String?): Boolean = input == null || input.isEmpty()

    fun isValidFirstName(
        context: Context,
        firstName: String?,
        regex: String = "^[a-zA-Z0-9._-]{3,20}$"
    ): Boolean {
        when {
            isNullOrEmpty(firstName) -> showToast(context, "Please enter first name.")
            !Pattern.matches(regex, firstName) -> showToast(
                context,
                "Please enter a valid first name."
            )
            else -> return true
        }
        return false
    }

    fun isValidLastName(
        context: Context,
        lastName: String?,
        regex: String = "^[a-zA-Z0-9._-]{3,20}$"
    ): Boolean {
        when {
            isNullOrEmpty(lastName) -> showToast(context, "Please enter last name.")
            !Pattern.matches(regex, lastName) -> showToast(
                context,
                "Please enter a valid last name."
            )
            else -> return true
        }
        return false
    }


    fun isValidEmail(context: Context, email: String?): Boolean {
        when {
            isNullOrEmpty(email) -> showToast(context, "Please enter email.")
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> showToast(
                context,
                "Please enter a valid email address."
            )
            else -> return true
        }
        return false
    }

    fun isValidMobile(
        context: Context,
        mobile: String?,
        regex: String = "^[0-9]{10}$"
    ): Boolean {
        when {
            isNullOrEmpty(mobile) -> showToast(context, "Please enter Mobile number first.")
            !Pattern.matches(regex, mobile) -> showToast(
                context,
                "Please enter a valid Mobile number."
            )
            else -> return true
        }
        return false
    }

    fun isValidPassword(context: Context, password: String?): Boolean {
        when {
            isNullOrEmpty(password) -> showToast(context, "Please enter password.")
            password!!.length < 6 -> showToast(
                context,
                "Password length should not be less than 6 characters"
            )
            password.length > 30 -> showToast(
                context,
                "Password length should not be greater than 30 characters"
            )
            else -> return true
        }
        return false
    }

    fun isValidConfirmPassword(context: Context, confirmPassword: String?): Boolean {
        when {
            isNullOrEmpty(confirmPassword) -> showToast(context, "Please enter password again.")
            confirmPassword!!.length < 6 -> showToast(
                context,
                "Password length should not be less than 6 characters"
            )
            confirmPassword.length > 30 -> showToast(
                context,
                "Password length should not be greater than 30 characters"
            )
            else -> return true
        }
        return false
    }

    fun areEqual(context: Context, password: String?, confirmPassword: String?): Boolean {
        return if (TextUtils.equals(password, confirmPassword)) {
            true
        } else {
            showToast(context, "Passwords are not equals")
            false
        }
    }
}