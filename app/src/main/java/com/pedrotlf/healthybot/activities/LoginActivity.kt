package com.pedrotlf.healthybot.activities

import android.content.Intent
import android.os.Bundle
import com.pedrotlf.healthybot.R
import com.pedrotlf.healthybot.RegistrationRuleBaseActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : RegistrationRuleBaseActivity() {

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
                startActivity(Intent(this, ChatMainActivity::class.java))
                finish()
            }
        }
    }
}
