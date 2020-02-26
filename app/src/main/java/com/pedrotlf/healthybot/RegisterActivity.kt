package com.pedrotlf.healthybot

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : RegistrationRuleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        configureRegister()

        btn_sign_in.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun configureRegister() {
        btn_sign_up.setOnClickListener {
            val user: String = username.text.toString()
            val pass: String = password.text.toString()
            val confirmPass: String = confirm_password.text.toString()

            if( usernameValidator(user) &&
                passwordValidator(pass, confirmPass)
            ){
                //registerRequest()
            }
        }
    }
}
