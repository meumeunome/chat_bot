package com.pedrotlf.healthybot.activities

import android.content.Intent
import android.os.Bundle
import com.pedrotlf.healthybot.R
import com.pedrotlf.healthybot.RegistrationRuleBaseActivity
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : RegistrationRuleBaseActivity() {

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
            val email: String = email.text.toString()
            val birth: String = birth_date.text.toString()

            if( usernameValidator(user) &&
                passwordValidator(pass, confirmPass) &&
                emailValidator(email) &&
                birthdayValidator(birth)
            ){
                //registerRequest()
                startActivity(Intent(this, ChatMainActivity::class.java))
                finish()
            }
        }
    }
}
