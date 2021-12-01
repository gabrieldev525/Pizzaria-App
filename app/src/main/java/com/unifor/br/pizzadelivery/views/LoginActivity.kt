package com.unifor.br.pizzadelivery.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.unifor.br.pizzadelivery.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        supportActionBar?.hide()
    }
}