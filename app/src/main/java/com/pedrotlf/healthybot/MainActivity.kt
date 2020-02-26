package com.pedrotlf.healthybot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_sign_in.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        btn_sign_up.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
