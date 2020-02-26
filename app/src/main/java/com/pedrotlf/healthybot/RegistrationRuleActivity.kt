package com.pedrotlf.healthybot

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("Registered")
open class RegistrationRuleActivity : AppCompatActivity() {
    protected fun usernameValidator(string: String): Boolean{
        val message: String = when{
            string.isEmpty() -> {
                "Username field is empty."
            }

            else -> {
                ""
            }
        }

        return showError(message)
    }

    protected fun passwordValidator(string: String, confirm: String? = null): Boolean{
        val message: String = when{
            string.isEmpty() -> "Password field is empty."

            confirm != null && confirm != string -> "Password and Confirm Password must be the same."

            else -> ""
        }

        return showError(message)
    }

    protected fun emailValidator(string: String): Boolean{
        val message: String = when{
            string.isEmpty() -> {
                "Email field is empty."
            }

            !string.contains("@") -> {
                "Email address is missing @"
            }

            else -> {
                ""
            }
        }

        return showError(message)
    }

    protected fun birthdayValidator(string: String): Boolean{
        val message: String = when{
            string.isEmpty() -> {
                "Birthday field is empty."
            }

            else -> {
                ""
            }
        }

        return showError(message)
    }

    private fun showError(message: String): Boolean {
        return if (message.isNotEmpty()) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            false
        } else {
            true
        }
    }
}