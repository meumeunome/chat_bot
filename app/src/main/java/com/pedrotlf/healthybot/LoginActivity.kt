package com.pedrotlf.healthybot

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : RegistrationRuleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        configureLogin()

        btn_sign_up.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

    private fun configureLogin() {
        btn_sign_in.setOnClickListener {
            val user: String = username.text.toString()
            val pass: String = password.text.toString()

            if( usernameValidator(user) &&
                passwordValidator(pass)
            ){
                //loginRequest()
            }
        }
    }
}
